-- Scenario 1: Transfer funds safely with rollback on error

CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_from_account IN NUMBER,
    p_to_account   IN NUMBER,
    p_amount       IN NUMBER
) AS
    v_from_balance NUMBER;
BEGIN
    SELECT Balance INTO v_from_balance
    FROM Accounts
    WHERE AccountID = p_from_account
    FOR UPDATE;

    IF v_from_balance < p_amount THEN
        RAISE_APPLICATION_ERROR(-20001, 'Not enough balance in account ' || p_from_account);
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
    DBMS_OUTPUT.PUT_LINE('Transfer successful. Amount: ' || p_amount);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: One of the account IDs does not exist.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error during transfer: ' || SQLERRM);
END;
/

-- test it
BEGIN
    SafeTransferFunds(1, 2, 200);
END;
/
