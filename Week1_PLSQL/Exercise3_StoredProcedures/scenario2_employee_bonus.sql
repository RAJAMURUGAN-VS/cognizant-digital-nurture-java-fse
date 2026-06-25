-- Scenario 2: Add bonus to employees in a given department

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department     IN VARCHAR2,
    p_bonus_percent  IN NUMBER
) AS
    v_rows NUMBER;
BEGIN
    UPDATE Employees
    SET Salary = Salary + (Salary * p_bonus_percent / 100)
    WHERE Department = p_department;

    v_rows := SQL%ROWCOUNT;
    COMMIT;

    IF v_rows = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_department);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Bonus applied to ' || v_rows || ' employee(s) in ' || p_department);
    END IF;
END;
/

-- test
BEGIN
    UpdateEmployeeBonus('IT', 15);
    UpdateEmployeeBonus('HR', 10);
    UpdateEmployeeBonus('Finance', 5);
END;
/
