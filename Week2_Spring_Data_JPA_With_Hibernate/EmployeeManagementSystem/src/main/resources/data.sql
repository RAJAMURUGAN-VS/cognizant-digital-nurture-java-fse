-- Sample seed data loaded by Spring Boot on startup
-- (spring.jpa.hibernate.ddl-auto=create-drop creates schema from entities first)

INSERT INTO department (id, name) VALUES (1, 'Engineering');
INSERT INTO department (id, name) VALUES (2, 'Human Resources');
INSERT INTO department (id, name) VALUES (3, 'Finance');
INSERT INTO department (id, name) VALUES (4, 'Marketing');

INSERT INTO employee (id, name, email, department_id, created_by, last_modified_by)
    VALUES (1, 'Alice Johnson', 'alice@example.com', 1, 'system', 'system');
INSERT INTO employee (id, name, email, department_id, created_by, last_modified_by)
    VALUES (2, 'Bob Smith',    'bob@example.com',   1, 'system', 'system');
INSERT INTO employee (id, name, email, department_id, created_by, last_modified_by)
    VALUES (3, 'Carol White',  'carol@example.com', 2, 'system', 'system');
INSERT INTO employee (id, name, email, department_id, created_by, last_modified_by)
    VALUES (4, 'David Brown',  'david@example.com', 3, 'system', 'system');
INSERT INTO employee (id, name, email, department_id, created_by, last_modified_by)
    VALUES (5, 'Eva Martinez', 'eva@example.com',   4, 'system', 'system');
