CREATE DATABASE IF NOT EXISTS employedemo;
USE employedemo;

CREATE TABLE IF NOT EXISTS employe (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age  INT NOT NULL
);

INSERT INTO employe (name, age) VALUES
    ('Ahmed Alaoui', 30),
    ('Salma Bennani', 27),
    ('Youssef Idrissi', 41);
