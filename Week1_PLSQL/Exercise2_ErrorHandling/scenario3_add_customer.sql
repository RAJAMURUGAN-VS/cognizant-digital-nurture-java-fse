-- Scenario 3: Add new customer, handle duplicate ID

CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_id      IN NUMBER,
    p_name    IN VARCHAR2,
    p_dob     IN DATE,
    p_balance IN NUMBER
) AS
BEGIN
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_id, p_name, p_dob, p_balance, SYSDATE);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Customer added: ' || p_name);

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Error: Customer with ID ' || p_id || ' already exists. Skipping insert.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Something went wrong: ' || SQLERRM);
END;
/

-- test
BEGIN
    AddNewCustomer(10, 'Meena Raj', TO_DATE('1995-01-01', 'YYYY-MM-DD'), 5000);
    AddNewCustomer(1, 'Duplicate John', TO_DATE('1980-01-01', 'YYYY-MM-DD'), 100);
END;
/
