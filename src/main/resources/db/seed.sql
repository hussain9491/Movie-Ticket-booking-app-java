USE cinebook;

INSERT INTO movies (title, genre, duration_min, show_time, price, poster_path)
VALUES
    ('Interstellar', 'Sci-Fi', 169, DATE_ADD(NOW(), INTERVAL 1 DAY), 12.50, ''),
    ('Inception', 'Sci-Fi', 148, DATE_ADD(NOW(), INTERVAL 2 DAY), 10.00, ''),
    ('The Dark Knight', 'Action', 152, DATE_ADD(NOW(), INTERVAL 3 DAY), 11.00, ''),
    ('Soul', 'Animation', 100, DATE_ADD(NOW(), INTERVAL 4 DAY), 8.50, ''),
    ('Whiplash', 'Drama', 106, DATE_ADD(NOW(), INTERVAL 5 DAY), 9.00, '')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- First run setup:
-- Register a user from the login screen, then promote it:
-- UPDATE users SET role='admin' WHERE username='your_username';

