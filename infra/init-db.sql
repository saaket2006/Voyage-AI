-- Safe database bootstrap procedure for Voyage-AI
-- Ensures the 'voyage' database exists with UTF-8 character encoding.
-- Does NOT drop or modify any existing databases (including 'pat' or 'voyage_ai').

CREATE DATABASE IF NOT EXISTS voyage CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant privileges to voyage_user if running under Docker Compose
GRANT ALL PRIVILEGES ON voyage.* TO 'voyage_user'@'%';
FLUSH PRIVILEGES;
