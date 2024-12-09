DROP DATABASE IF EXISTS library_management;
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Drop tables if they exist
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Authors;
DROP TABLE IF EXISTS Publishers;
DROP TABLE IF EXISTS Categories;
DROP TABLE IF EXISTS Documents;
DROP TABLE IF EXISTS BorrowingRecords;
DROP TABLE IF EXISTS Reservations;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Admins;
-- DROP TABLE IF EXISTS AdminActions;
DROP TABLE IF EXISTS Fines;

-- Table for managing users
CREATE TABLE Users (
                       user_id INT AUTO_INCREMENT,
                       full_name VARCHAR(255) NOT NULL,
                       user_name VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       user_role ENUM('reader', 'admin') DEFAULT 'reader',
                       account_status ENUM('active', 'suspended') DEFAULT 'active',
                       join_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       last_login DATETIME,
    -- login_attempts INT DEFAULT 0,
                       account_locked BOOLEAN DEFAULT FALSE,
                       PRIMARY KEY(user_id)
);

-- Table for managing authors
CREATE TABLE Authors (
                         author_id INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         PRIMARY KEY(author_id)
);

-- Table for managing publishers
CREATE TABLE Publishers (
                            publisher_id INT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL UNIQUE,
    -- address TEXT NOT NULL,
    -- contact_info VARCHAR(255),
                            PRIMARY KEY(publisher_id)
);

-- Table for categorizing documents
CREATE TABLE Categories (
                            category_id INT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
    -- description TEXT,
                            PRIMARY KEY(category_id)
);

-- Table for managing documents
CREATE TABLE Documents (
                           title VARCHAR(255) NOT NULL,
                           author_id INT NOT NULL,
                           publisher_id INT NOT NULL,
                           isbn VARCHAR(13) UNIQUE NOT NULL,
                           category_id INT NOT NULL,
                           publication_year INT CHECK (publication_year BETWEEN 1900 AND 2024),
                           quantity INT NOT NULL,
                           pages int not null,
                           description TEXT,
                           location VARCHAR(255) NOT NULL,
                           preview_link TEXT,
                           book_image TEXT,
                           added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (author_id) REFERENCES Authors(author_id) ON DELETE  RESTRICT ON UPDATE CASCADE,
                           FOREIGN KEY (publisher_id) REFERENCES Publishers(publisher_id) ON DELETE  RESTRICT ON UPDATE CASCADE,
                           FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE  RESTRICT ON UPDATE CASCADE
);

-- Table for recording borrowing transactions
CREATE TABLE BorrowingRecords (
                                  record_id INT NOT NULL AUTO_INCREMENT,
                                  user_id INT NOT NULL,
                                  isbn VARCHAR(13) NOT NULL,
                                  borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  return_date DATETIME, -- Can be NULL if the document has not been returned
                                  status ENUM('borrowed', 'returned', 'late', 'lost') DEFAULT 'borrowed',
                                  PRIMARY KEY(record_id),
                                  FOREIGN KEY (user_id) REFERENCES Users(user_id) ,
                                  FOREIGN KEY (isbn) REFERENCES Documents(isbn)
);

-- Table for reserving documents
CREATE TABLE Reservations (
                              reservation_id INT NOT NULL AUTO_INCREMENT,
                              user_id INT NOT NULL,
                              isbn VARCHAR(13) NOT NULL,
                              reservation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              status ENUM('active', 'fulfilled', 'cancelled') DEFAULT 'active',
                              PRIMARY KEY(reservation_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id) ,
                              FOREIGN KEY (isbn) REFERENCES Documents(isbn)
);

-- Table for reviewing documents
CREATE TABLE Reviews (
                         review_id INT NOT NULL AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         isbn VARCHAR(13) NOT NULL,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment TEXT,
                         review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY(review_id),
                         FOREIGN KEY (user_id) REFERENCES Users(user_id) ,
                         FOREIGN KEY (isbn) REFERENCES Documents(isbn)
);

-- Table for managing administrators
CREATE TABLE Admins (
                        admin_id INT NOT NULL AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY(admin_id),
                        FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE RESTRICT ON UPDATE  CASCADE
);



-- Table for managing overdue fines
CREATE TABLE Fines (
                       fine_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT,
                       record_id INT,    -- Links to borrowing records
                       fine_amount DECIMAL(9,0) NOT NULL,
                       due_date DATE NOT NULL,
                       status ENUM('UNPAID', 'PAID') DEFAULT 'UNPAID',
                       FOREIGN KEY (user_id) REFERENCES Users(user_id),
                       FOREIGN KEY (record_id) REFERENCES BorrowingRecords(record_id)
);

-- table to save favourite books
create table FavouriteBooks (
                        favourite_id int auto_increment primary key,
                        user_id int not null,
                        isbn VARCHAR(13) NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES Users(user_id),
                        FOREIGN KEY (isbn) REFERENCES Documents(isbn)
);

-- create event to automatically increase fine amount day by day
set global event_scheduler = ON;
drop event if exists increment_fine_amount_daily;
create event increment_fine_amount_daily
    on schedule every 1 day
        starts current_timestamp
    do update fines
    set fine_amount = fine_amount + 5000
    where status = 'UNPAID';

-- insert data if user_role is admin
DELIMITER //

CREATE TRIGGER after_user_insert
    AFTER INSERT ON Users
    FOR EACH ROW
BEGIN
    IF NEW.user_role = 'admin' THEN
        INSERT INTO Admins (user_id, assigned_date)
        VALUES (NEW.user_id, NEW.join_date);
    END IF;
END;

// DELIMITER ;
