-- Scenario 2: Deduct annual maintenance fee from all accounts

DECLARE
    v_annual_fee CONSTANT NUMBER := 200;

    CURSOR ApplyAnnualFee IS
        SELECT AccountID, Balance FROM Accounts FOR UPDATE;
BEGIN
    FOR rec IN ApplyAnnualFee LOOP
        IF rec.Balance >= v_annual_fee THEN
            UPDATE Accounts
            SET Balance = Balance - v_annual_fee,
                LastModified = SYSDATE
            WHERE AccountID = rec.AccountID;

            DBMS_OUTPUT.PUT_LINE('Fee deducted from account ' || rec.AccountID);
        ELSE
            DBMS_OUTPUT.PUT_LINE('Account ' || rec.AccountID || ' has low balance, skipping fee.');
        END IF;
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Annual fee processing done.');
END;
/
