-- Scenario 3: Transfer funds between two accounts with balance check

CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account IN NUMBER,
    p_to_account   IN NUMBER,
    p_amount       IN NUMBER
) AS
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance
    FROM Accounts
    WHERE AccountID = p_from_account
    FOR UPDATE;

    IF v_balance < p_amount THEN
        DBMS_OUTPUT.PUT_LINE('Transfer failed: insufficient balance in account ' || p_from_account);
        RETURN;
    END IF;

    UPDATE Accounts
    SET Balance = Balance - p_amount,
        LastModified = SYSDATE
    WHERE AccountID = p_from_account;

    UPDATE Accounts
    SET Balance = Balance + p_amount,
        LastModified = SYSDATE
    WHERE AccountID = p_to_account;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transferred ' || p_amount || ' from account ' || p_from_account || ' to ' || p_to_account);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Account not found.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Transfer error: ' || SQLERRM);
END;
/

-- test
BEGIN
    TransferFunds(1, 2, 100);
    TransferFunds(2, 1, 99999);
END;
/
