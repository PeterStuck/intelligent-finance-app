DROP DATABASE IF EXISTS finance;

CREATE DATABASE finance;

GRANT ALL PRIVILEGES ON finance.* TO 'root'@'localhost';
GRANT ALL PRIVILEGES ON finance.* TO 'root'@'%';