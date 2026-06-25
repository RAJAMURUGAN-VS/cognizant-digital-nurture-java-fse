-- Scenario 1: CustomerManagement package

CREATE OR REPLACE PACKAGE CustomerManagement AS
    PROCEDURE AddCustomer(
        p_id      IN NUMBER,
        p_name    IN VARCHAR2,
        p_dob     IN DATE,
        p_balance IN NUMBER
    );

    PROCEDURE UpdateCustomer(
        p_id      IN NUMBER,
        p_name    IN VARCHAR2,
        p_balance IN NUMBER
    );

    FUNCTION GetCustomerBalance(p_id IN NUMBER) RETURN NUMBER;
END CustomerManagement;
/

CREATE OR REPLACE PACKAGE BODY CustomerManagement AS

    PROCEDURE AddCustomer(
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
            DBMS_OUTPUT.PUT_LINE('Customer ID ' || p_id || ' already exists.');
    END AddCustomer;

    PROCEDURE UpdateCustomer(
        p_id      IN NUMBER,
        p_name    IN VARCHAR2,
        p_balance IN NUMBER
    ) AS
    BEGIN
        UPDATE Customers
        SET Name = p_name, Balance = p_balance, LastModified = SYSDATE
        WHERE CustomerID = p_id;

        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Customer ID ' || p_id || ' not found.');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('Customer ' || p_id || ' updated.');
        END IF;
    END UpdateCustomer;

    FUNCTION GetCustomerBalance(p_id IN NUMBER) RETURN NUMBER AS
        v_balance NUMBER;
    BEGIN
        SELECT Balance INTO v_balance
        FROM Customers
        WHERE CustomerID = p_id;
        RETURN v_balance;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN -1;
    END GetCustomerBalance;

END CustomerManagement;
/

-- test
BEGIN
    CustomerManagement.AddCustomer(20, 'Priya Nair', TO_DATE('1992-05-10', 'YYYY-MM-DD'), 3000);
    CustomerManagement.UpdateCustomer(20, 'Priya Nair', 3500);
    DBMS_OUTPUT.PUT_LINE('Balance: ' || CustomerManagement.GetCustomerBalance(20));
END;
/
