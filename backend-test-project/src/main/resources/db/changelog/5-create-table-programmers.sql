CREATE TABLE programmers
(
    programmer_id      INT PRIMARY KEY AUTO_INCREMENT,
    name               VARCHAR(255) NOT NULL,
    address_id         INT UNIQUE ,
    birth_date_id      INT UNIQUE ,
    phone_number       VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    project_id         INT,
    project_manager_id INT,
    responsibility     ENUM ('BACKEND', 'FRONTEND', 'FULLSTACK'),
    is_apprentice      BIT     NOT NULL,
    deleted            BIT          NOT NULL DEFAULT FALSE,
    FOREIGN KEY (address_id) REFERENCES addresses (address_id),
    FOREIGN KEY (birth_date_id) REFERENCES birth_dates (birth_date_id),
    FOREIGN KEY (project_id) REFERENCES projects (project_id),
    FOREIGN KEY (project_manager_id) REFERENCES project_managers (project_manager_id)
);