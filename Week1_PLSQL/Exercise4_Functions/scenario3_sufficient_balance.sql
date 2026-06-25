-- Scenario 3: Check if account has enough balance

CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id IN NUMBER,
    p_amount     IN NUMBER
) RETURN BOOLEAN AS
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance
    FROM Accounts
    WHERE AccountID = p_account_id;

    IF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Account ' || p_account_id || ' not found.');
        RETURN FALSE;
END;
/

-- test
DECLARE
    v_result BOOLEAN;
BEGIN
    v_result := HasSufficientBalance(1, 500);
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Account 1 has sufficient balance for 500.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Account 1 does NOT have sufficient balance for 500.');
    END IF;

    v_result := HasSufficientBalance(1, 99999);
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Account 1 has sufficient balance for 99999.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Account 1 does NOT have sufficient balance for 99999.');
    END IF;
END;
/
