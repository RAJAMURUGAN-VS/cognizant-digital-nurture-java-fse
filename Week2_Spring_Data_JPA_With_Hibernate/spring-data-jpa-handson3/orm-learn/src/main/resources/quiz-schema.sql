-- ================================================================
-- Hands-on 3: Quiz application schema + sample data
-- Run this in MySQL Workbench on the ormlearn schema
-- ================================================================

USE ormlearn;

-- ----------------------------------------------------------------
-- Master tables
-- ----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `user` (
    `us_id`       INT NOT NULL AUTO_INCREMENT,
    `us_username` VARCHAR(50) NOT NULL,
    `us_email`    VARCHAR(100),
    PRIMARY KEY (`us_id`)
);

CREATE TABLE IF NOT EXISTS `question` (
    `qu_id`    INT NOT NULL AUTO_INCREMENT,
    `qu_text`  VARCHAR(500) NOT NULL,
    `qu_score` DECIMAL(5, 2) DEFAULT 1.0,
    PRIMARY KEY (`qu_id`)
);

CREATE TABLE IF NOT EXISTS `options` (
    `op_id`          INT NOT NULL AUTO_INCREMENT,
    `op_text`        VARCHAR(200) NOT NULL,
    `op_is_correct`  TINYINT(1) DEFAULT 0,
    `op_qu_id`       INT NOT NULL,
    PRIMARY KEY (`op_id`),
    CONSTRAINT `fk_option_question`
        FOREIGN KEY (`op_qu_id`) REFERENCES `question` (`qu_id`)
);

-- ----------------------------------------------------------------
-- Attempt tables
-- ----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `attempt` (
    `at_id`           INT NOT NULL AUTO_INCREMENT,
    `at_us_id`        INT NOT NULL,
    `at_attempted_on` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`at_id`),
    CONSTRAINT `fk_attempt_user`
        FOREIGN KEY (`at_us_id`) REFERENCES `user` (`us_id`)
);

CREATE TABLE IF NOT EXISTS `attempt_question` (
    `aq_id`    INT NOT NULL AUTO_INCREMENT,
    `aq_at_id` INT NOT NULL,
    `aq_qu_id` INT NOT NULL,
    PRIMARY KEY (`aq_id`),
    CONSTRAINT `fk_aq_attempt`   FOREIGN KEY (`aq_at_id`) REFERENCES `attempt`  (`at_id`),
    CONSTRAINT `fk_aq_question`  FOREIGN KEY (`aq_qu_id`) REFERENCES `question` (`qu_id`)
);

CREATE TABLE IF NOT EXISTS `attempt_option` (
    `ao_id`        INT NOT NULL AUTO_INCREMENT,
    `ao_aq_id`     INT NOT NULL,
    `ao_op_id`     INT NOT NULL,
    `ao_selected`  TINYINT(1) DEFAULT 0,
    PRIMARY KEY (`ao_id`),
    CONSTRAINT `fk_ao_attempt_question` FOREIGN KEY (`ao_aq_id`) REFERENCES `attempt_question` (`aq_id`),
    CONSTRAINT `fk_ao_option`           FOREIGN KEY (`ao_op_id`) REFERENCES `options`          (`op_id`)
);

-- ----------------------------------------------------------------
-- Sample data
-- ----------------------------------------------------------------

-- Users
INSERT INTO `user` (us_username, us_email) VALUES ('alice', 'alice@example.com');
INSERT INTO `user` (us_username, us_email) VALUES ('bob',   'bob@example.com');

-- Questions
INSERT INTO `question` (qu_text, qu_score)
    VALUES ('What is the extension of the hyper text markup language file?', 1.0);
INSERT INTO `question` (qu_text, qu_score)
    VALUES ('What is the maximum level of heading tag can be used in a HTML page?', 1.0);
INSERT INTO `question` (qu_text, qu_score)
    VALUES ('The HTML document itself begins with <html> and ends </html>. State True or False', 1.0);
INSERT INTO `question` (qu_text, qu_score)
    VALUES ('Choose the right option to store text value in a variable', 0.5);

-- Options for Question 1
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('.xhtm', 0, 1);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('.ht',   0, 1);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('.html', 1, 1);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('.htmx', 0, 1);

-- Options for Question 2
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('5', 0, 2);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('3', 0, 2);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('4', 0, 2);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('6', 1, 2);

-- Options for Question 3
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('false', 0, 3);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('true',  1, 3);

-- Options for Question 4
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('''John''', 1, 4);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('John',    0, 4);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('"John"',  1, 4);
INSERT INTO `options` (op_text, op_is_correct, op_qu_id) VALUES ('/John/',  0, 4);

-- Attempt by alice (user id=1)
INSERT INTO `attempt` (at_us_id, at_attempted_on) VALUES (1, '2024-01-15 10:00:00');

-- Attempt questions for attempt 1
INSERT INTO `attempt_question` (aq_at_id, aq_qu_id) VALUES (1, 1);
INSERT INTO `attempt_question` (aq_at_id, aq_qu_id) VALUES (1, 2);
INSERT INTO `attempt_question` (aq_at_id, aq_qu_id) VALUES (1, 3);
INSERT INTO `attempt_question` (aq_at_id, aq_qu_id) VALUES (1, 4);

-- Attempt options for attempt question 1 (Q1: .html selected = correct)
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (1, 1, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (1, 2, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (1, 3, 1);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (1, 4, 0);

-- Attempt options for attempt question 2 (Q2: '3' selected = wrong, answer is '6')
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (2, 5, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (2, 6, 1);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (2, 7, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (2, 8, 0);

-- Attempt options for attempt question 3 (Q3: 'true' selected = correct)
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (3, 9,  0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (3, 10, 1);

-- Attempt options for attempt question 4 (Q4: 'John' selected = wrong)
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (4, 11, 1);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (4, 12, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (4, 13, 0);
INSERT INTO `attempt_option` (ao_aq_id, ao_op_id, ao_selected) VALUES (4, 14, 0);
