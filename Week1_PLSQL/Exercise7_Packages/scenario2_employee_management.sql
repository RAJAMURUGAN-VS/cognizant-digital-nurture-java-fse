-- Scenario 2: EmployeeManagement package

CREATE OR REPLACE PACKAGE EmployeeManagement AS
    PROCEDURE HireEmployee(
        p_id         IN NUMBER,
        p_name       IN VARCHAR2,
        p_position   IN VARCHAR2,
        p_salary     IN NUMBER,
        p_department IN VARCHAR2,
        p_hire_date  IN DATE
    );

    PROCEDURE UpdateEmployeeDetails(
        p_id       IN NUMBER,
        p_position IN VARCHAR2,
        p_salary   IN NUMBER
    );

    FUNCTION CalculateAnnualSalary(p_id IN NUMBER) RETURN NUMBER;
END EmployeeManagement;
/

CREATE OR REPLACE PACKAGE BODY EmployeeManagement AS

    PROCEDURE HireEmployee(
        p_id         IN NUMBER,
        p_name       IN VARCHAR2,
        p_position   IN VARCHAR2,
        p_salary     IN NUMBER,
        p_department IN VARCHAR2,
        p_hire_date  IN DATE
    ) AS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_id, p_name, p_position, p_salary, p_department, p_hire_date);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Hired: ' || p_name);
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            DBMS_OUTPUT.PUT_LINE('Employee ID ' || p_id || ' already exists.');
    END HireEmployee;

    PROCEDURE UpdateEmployeeDetails(
        p_id       IN NUMBER,
        p_position IN VARCHAR2,
        p_salary   IN NUMBER
    ) AS
    BEGIN
        UPDATE Employees
        SET Position = p_position, Salary = p_salary
        WHERE EmployeeID = p_id;

        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Employee ' || p_id || ' not found.');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('Employee ' || p_id || ' details updated.');
        END IF;
    END UpdateEmployeeDetails;

    FUNCTION CalculateAnnualSalary(p_id IN NUMBER) RETURN NUMBER AS
        v_salary NUMBER;
    BEGIN
        SELECT Salary INTO v_salary
        FROM Employees
        WHERE EmployeeID = p_id;
        RETURN v_salary * 12;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN 0;
    END CalculateAnnualSalary;

END EmployeeManagement;
/

-- test
BEGIN
    EmployeeManagement.HireEmployee(10, 'Karan Mehta', 'Analyst', 50000, 'IT', SYSDATE);
    EmployeeManagement.UpdateEmployeeDetails(10, 'Senior Analyst', 60000);
    DBMS_OUTPUT.PUT_LINE('Annual salary: ' || EmployeeManagement.CalculateAnnualSalary(10));
END;
/
