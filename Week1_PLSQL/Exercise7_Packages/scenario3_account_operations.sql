-- Scenario 3: AccountOperations package

CREATE OR REPLACE PACKAGE AccountOperations AS
    PROCEDURE OpenAccount(
        p_account_id   IN NUMBER,
        p_customer_id  IN NUMBER,
        p_account_type IN VARCHAR2,
        p_balance      IN NUMBER
    );

    PROCEDURE CloseAccount(p_account_id IN NUMBER);

    FUNCTION GetTotalBalance(p_customer_id IN NUMBER) RETURN NUMBER;
END AccountOperations;
/

CREATE OR REPLACE PACKAGE BODY AccountOperations AS

    PROCEDURE OpenAccount(
        p_account_id   IN NUMBER,
        p_customer_id  IN NUMBER,
        p_account_type IN VARCHAR2,
        p_balance      IN NUMBER
    ) AS
    BEGIN
        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
        VALUES (p_account_id, p_customer_id, p_account_type, p_balance, SYSDATE);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' opened for customer ' || p_customer_id);
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            DBMS_OUTPUT.PUT_LINE('Account ID ' || p_account_id || ' already exists.');
    END OpenAccount;

    PROCEDURE CloseAccount(p_account_id IN NUMBER) AS
    BEGIN
        DELETE FROM Accounts WHERE AccountID = p_account_id;

        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' not found.');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' closed.');
        END IF;
    END CloseAccount;

    FUNCTION GetTotalBalance(p_customer_id IN NUMBER) RETURN NUMBER AS
        v_total NUMBER;
    BEGIN
        SELECT NVL(SUM(Balance), 0) INTO v_total
        FROM Accounts
        WHERE CustomerID = p_customer_id;
        RETURN v_total;
    END GetTotalBalance;

END AccountOperations;
/

-- test
BEGIN
    AccountOperations.OpenAccount(100, 1, 'Savings', 2000);
    DBMS_OUTPUT.PUT_LINE('Total balance for customer 1: ' || AccountOperations.GetTotalBalance(1));
    AccountOperations.CloseAccount(100);
    DBMS_OUTPUT.PUT_LINE('Total balance after closing: ' || AccountOperations.GetTotalBalance(1));
END;
/
