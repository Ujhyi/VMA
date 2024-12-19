SELECT * FROM users;

DROP TABLE users;

CREATE TABLE users (
                       user_id INT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(250) NOT NULL,
                       isic VARCHAR(250) NOT NULL,
                       school VARCHAR(250) NOT NULL,
                       PRIMARY KEY (user_id)
);

SHOW TABLES;
