-- Scenario 2: Update employee salary by a given percentage

CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_employee_id    IN NUMBER,
    p_percentage     IN NUMBER
) AS
    v_current_salary NUMBER;
BEGIN
    SELECT Salary INTO v_current_salary
    FROM Employees
    WHERE EmployeeID = p_employee_id;

    UPDATE Employees
    SET Salary = Salary + (Salary * p_percentage / 100)
    WHERE EmployeeID = p_employee_id;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Salary updated for employee ' || p_employee_id);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Error: Employee ID ' || p_employee_id || ' not found.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Unexpected error: ' || SQLERRM);
END;
/

-- test
BEGIN
    UpdateSalary(1, 10);
    UpdateSalary(99, 10);
END;
/
