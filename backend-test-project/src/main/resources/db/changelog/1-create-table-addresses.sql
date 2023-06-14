CREATE TABLE addresses
(
    address_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    zip_code   INT             NOT NULL,
    city       VARCHAR(255)    NOT NULL,
    street     VARCHAR(255)    NOT NULL
);