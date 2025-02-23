drop database if exists atm_db;
CREATE DATABASE atm_db;
USE atm_db;


CREATE TABLE account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         account_number VARCHAR(10) UNIQUE NOT NULL,
                         full_name VARCHAR(255),
                         balance DOUBLE DEFAULT 0.0
);

CREATE TABLE transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             type VARCHAR(50),
                             amount DOUBLE,
                             transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             from_account_number VARCHAR(50),
                             to_account_number VARCHAR(50)
);