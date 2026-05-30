USE cinebook;

-- Reset in FK-safe order
DELETE FROM bookings;
DELETE FROM seats;
DELETE FROM movies;
DELETE FROM users;

-- Reset auto-increment counters for predictable IDs in demos
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE movies AUTO_INCREMENT = 1;
ALTER TABLE seats AUTO_INCREMENT = 1;
ALTER TABLE bookings AUTO_INCREMENT = 1;

-- Seed users
-- Passwords:
-- admin / Admin@123
-- john / User@123
-- sara / User@123
INSERT INTO users (id, username, password, email, role, active, created_at)
VALUES
    (1, 'admin', '$2a$10$DBP5t9bOuVVnxp4BGKX8aOZr4xC4SvvIEsfv.OcagrSSsaEgLJJvi', 'admin@cinebook.local', 'admin', 1, NOW()),
    (2, 'john', '$2a$10$O2B0KvVfa/j2sJUkryU3rekIiBWNIXLIqOu2M6RvOB1PhD4rxSXNy', 'john@cinebook.local', 'user', 1, NOW()),
    (3, 'sara', '$2a$10$O2B0KvVfa/j2sJUkryU3rekIiBWNIXLIqOu2M6RvOB1PhD4rxSXNy', 'sara@cinebook.local', 'user', 1, NOW());

-- Seed movies
INSERT INTO movies (id, title, genre, duration_min, show_time, price, poster_path)
VALUES
    (1, 'Interstellar', 'Sci-Fi', 169, '2026-06-01 19:30:00', 12.50, ''),
    (2, 'The Dark Knight', 'Action', 152, '2026-06-01 21:00:00', 11.00, ''),
    (3, 'Soul', 'Animation', 100, '2026-06-02 18:00:00', 8.50, '');

-- Seed seats (60 seats/movie => 5 rows x 12 columns)
INSERT INTO seats (movie_id, seat_number, row_label, is_booked, seat_type)
WITH RECURSIVE seat_numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM seat_numbers WHERE n < 60
)
SELECT
    m.id,
    CONCAT(CHAR(65 + FLOOR((sn.n - 1) / 12)), ((sn.n - 1) % 12) + 1),
    CHAR(65 + FLOOR((sn.n - 1) / 12)),
    0,
    CASE
        WHEN FLOOR((sn.n - 1) / 12) = 0 OR ((sn.n - 1) % 12) + 1 > 10 THEN 'vip'
        ELSE 'standard'
    END
FROM movies m
CROSS JOIN seat_numbers sn;

-- Seed bookings
INSERT INTO bookings (booking_code, user_id, movie_id, seat_id, booking_date, total_price, status, payment_ref)
VALUES
    ('CB-20260524-1001', 2, 1, (SELECT id FROM seats WHERE movie_id = 1 AND seat_number = 'A1'), '2026-05-24 10:15:00', 18.75, 'confirmed', 'PAY-REF-1001'),
    ('CB-20260524-1002', 3, 1, (SELECT id FROM seats WHERE movie_id = 1 AND seat_number = 'B2'), '2026-05-24 10:35:00', 12.50, 'cancelled', 'PAY-REF-1002'),
    ('CB-20260524-1003', 2, 2, (SELECT id FROM seats WHERE movie_id = 2 AND seat_number = 'A1'), '2026-05-24 11:10:00', 16.50, 'confirmed', 'PAY-REF-1003'),
    ('CB-20260524-1004', 3, 3, (SELECT id FROM seats WHERE movie_id = 3 AND seat_number = 'A2'), '2026-05-24 12:05:00', 12.75, 'confirmed', 'PAY-REF-1004');

-- Keep seat flags aligned with booking status
UPDATE seats SET is_booked = 0;
UPDATE seats s
JOIN bookings b ON b.seat_id = s.id AND b.status = 'confirmed'
SET s.is_booked = 1;

-- Optional sanity checks
SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'movies', COUNT(*) FROM movies
UNION ALL
SELECT 'seats', COUNT(*) FROM seats
UNION ALL
SELECT 'bookings', COUNT(*) FROM bookings;

SELECT movie_id, COUNT(*) AS seat_count
FROM seats
GROUP BY movie_id
ORDER BY movie_id;

