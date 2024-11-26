DROP DATABASE TWITTER;
CREATE DATABASE TWITTER;

USE TWITTER;

CREATE TABLE USER
(
    id           VARCHAR(15) NOT NULL PRIMARY KEY,
    pwd          VARCHAR(15) NOT NULL,
    first_name   VARCHAR(15),
    last_name    VARCHAR(15),
    email        VARCHAR(40) NOT NULL,
    gender       VARCHAR(10),
    phone_number VARCHAR(13),
    birth        DATE
);

CREATE TABLE POST
(
    id        INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content   TEXT NOT NULL,
    user_id   VARCHAR(15),
    create_at DATETIME(6),
    FOREIGN KEY (user_id) REFERENCES USER (id)
);

CREATE TABLE POST_LIKE
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(15),
    post_id INT,
    FOREIGN KEY (user_id) REFERENCES USER (id),
    FOREIGN KEY (post_id) REFERENCES POST (id)
);

CREATE TABLE BOOKMARK_GROUP
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(15),
    post_id INT,
    FOREIGN KEY (user_id) REFERENCES USER (id),
    FOREIGN KEY (post_id) REFERENCES POST (id)
);

CREATE TABLE POST_HASHTAG
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    hash_name VARCHAR(50) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES POST (id)
);

CREATE TABLE COMMENT
(
    id               INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id          VARCHAR(15),
    post_id          INT,
    comment          TEXT,
    FOREIGN KEY (user_id) REFERENCES USER (id),
    FOREIGN KEY (post_id) REFERENCES POST (id)
);

CREATE TABLE FOLLOW
(
    follow_id   VARCHAR(15),
    followed_id VARCHAR(15),
    PRIMARY KEY (follow_id, followed_id),
    FOREIGN KEY (follow_id) REFERENCES USER (id),
    FOREIGN KEY (followed_id) REFERENCES USER (id),
    CONSTRAINT no_self_follow CHECK (follow_id != followed_id)
);
