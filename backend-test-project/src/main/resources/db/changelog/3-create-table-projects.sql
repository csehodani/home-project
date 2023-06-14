CREATE TABLE projects
(
    project_id  INT PRIMARY KEY AUTO_INCREMENT,
    client      VARCHAR(255),
    start_date  VARCHAR(255),
    description VARCHAR(255),
    deleted     BIT DEFAULT FALSE
);
