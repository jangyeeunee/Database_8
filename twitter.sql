DROP DATABASE TWITTER;
CREATE DATABASE TWITTER;

USE TWITTER;

CREATE TABLE USER
(
    id           VARCHAR(15) NOT NULL PRIMARY KEY,
    pwd          VARCHAR(15) NOT NULL,
    first_name   VARCHAR(15),
    last_name    VARCHAR(15),
    email        VARCHAR(20) NOT NULL,
    gender       VARCHAR(5),
    phone_number VARCHAR(11),
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

CREATE TABLE HASHTAG
(
    id   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE POST_HASHTAG
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    FOREIGN KEY (id) REFERENCES POST (id)
);

CREATE TABLE COMMENT
(
    id               INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id          VARCHAR(15),
    post_id          INT,
    comment          TEXT,
    child_comment_id INT NULL,
    FOREIGN KEY (user_id) REFERENCES USER (id),
    FOREIGN KEY (post_id) REFERENCES POST (id),
    FOREIGN KEY (child_comment_id) REFERENCES COMMENT (id) ON DELETE CASCADE
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
