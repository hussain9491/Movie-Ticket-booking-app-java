CREATE DATABASE IF NOT EXISTS cinebook;
USE cinebook;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    role ENUM('user', 'admin') NOT NULL DEFAULT 'user',
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    genre VARCHAR(40) NOT NULL,
    duration_min INT NOT NULL,
    show_time DATETIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    poster_path VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS seats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    row_label VARCHAR(2) NOT NULL,
    is_booked TINYINT(1) NOT NULL DEFAULT 0,
    seat_type ENUM('standard', 'vip') NOT NULL DEFAULT 'standard',
    UNIQUE KEY uk_movie_seat (movie_id, seat_number),
    CONSTRAINT fk_seat_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_code VARCHAR(32) NOT NULL,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    seat_id INT NOT NULL,
    booking_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('confirmed', 'cancelled') NOT NULL DEFAULT 'confirmed',
    payment_ref VARCHAR(60) NOT NULL,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE RESTRICT,
    CONSTRAINT fk_booking_seat FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE RESTRICT
);

CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_date ON bookings(booking_date);
CREATE INDEX idx_movies_showtime ON movies(show_time);

