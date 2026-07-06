-- ================================================================
-- Hands-on 3,4,5,6: Payroll schema DDL + sample data
-- Run in MySQL Workbench / client before starting the application
-- ================================================================

USE ormlearn;

-- Department table
CREATE TABLE IF NOT EXISTS `department` (
    `dp_id`   INT NOT NULL AUTO_INCREMENT,
    `dp_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`dp_id`)
);

-- Skill table
CREATE TABLE IF NOT EXISTS `skill` (
    `sk_id`   INT NOT NULL AUTO_INCREMENT,
    `sk_name` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`sk_id`)
);

-- Employee table
CREATE TABLE IF NOT EXISTS `employee` (
    `em_id`            INT NOT NULL AUTO_INCREMENT,
    `em_name`          VARCHAR(100) NOT NULL,
    `em_salary`        DOUBLE,
    `em_permanent`     TINYINT(1),
    `em_date_of_birth` DATE,
    `em_dp_id`         INT,
    PRIMARY KEY (`em_id`),
    CONSTRAINT `fk_employee_department`
        FOREIGN KEY (`em_dp_id`) REFERENCES `department` (`dp_id`)
);

-- Employee-Skill join table (Many-to-Many)
CREATE TABLE IF NOT EXISTS `employee_skill` (
    `es_em_id` INT NOT NULL,
    `es_sk_id` INT NOT NULL,
    PRIMARY KEY (`es_em_id`, `es_sk_id`),
    CONSTRAINT `fk_es_employee` FOREIGN KEY (`es_em_id`) REFERENCES `employee` (`em_id`),
    CONSTRAINT `fk_es_skill`    FOREIGN KEY (`es_sk_id`) REFERENCES `skill` (`sk_id`)
);

-- ----------------------------------------------------------------
-- Sample data
-- ----------------------------------------------------------------

-- Departments
INSERT INTO department (dp_name) VALUES ('Engineering');
INSERT INTO department (dp_name) VALUES ('Human Resources');
INSERT INTO department (dp_name) VALUES ('Finance');
INSERT INTO department (dp_name) VALUES ('Marketing');

-- Skills
INSERT INTO skill (sk_name) VALUES ('Java');
INSERT INTO skill (sk_name) VALUES ('Spring Boot');
INSERT INTO skill (sk_name) VALUES ('MySQL');
INSERT INTO skill (sk_name) VALUES ('Python');
INSERT INTO skill (sk_name) VALUES ('Leadership');
INSERT INTO skill (sk_name) VALUES ('Communication');

-- Employees
INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('Alice Johnson', 85000.00, 1, '1990-03-15', 1);

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('Bob Smith', 72000.00, 1, '1988-07-22', 1);

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('Carol White', 65000.00, 0, '1993-11-05', 2);

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('David Brown', 90000.00, 1, '1985-01-30', 3);

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('Eva Martinez', 78000.00, 1, '1992-06-18', 4);

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id)
VALUES ('Frank Lee', 68000.00, 0, '1995-09-12', 1);

-- Employee-Skill relationships
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (1, 1); -- Alice: Java
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (1, 2); -- Alice: Spring Boot
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (1, 3); -- Alice: MySQL
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (2, 1); -- Bob: Java
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (2, 4); -- Bob: Python
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (3, 5); -- Carol: Leadership
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (3, 6); -- Carol: Communication
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (4, 3); -- David: MySQL
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (5, 6); -- Eva: Communication
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (6, 1); -- Frank: Java
INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES (6, 2); -- Frank: Spring Boot
