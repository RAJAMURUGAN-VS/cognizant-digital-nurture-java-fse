-- Scenario 1: Apply 1% monthly interest to all savings accounts

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest AS
BEGIN
    UPDATE Accounts
    SET Balance = Balance + (Balance * 0.01),
        LastModified = SYSDATE
    WHERE AccountType = 'Savings';

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Monthly interest applied to all savings accounts. Rows updated: ' || SQL%ROWCOUNT);
END;
/

-- run it
BEGIN
    ProcessMonthlyInterest;
END;
/
