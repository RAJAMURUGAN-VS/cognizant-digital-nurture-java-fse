-- Scenario 1: Print monthly statements for all customers

DECLARE
    CURSOR GenerateMonthlyStatements IS
        SELECT t.TransactionID, t.AccountID, t.TransactionDate, t.Amount, t.TransactionType,
               c.Name
        FROM Transactions t
        JOIN Accounts a ON t.AccountID = a.AccountID
        JOIN Customers c ON a.CustomerID = c.CustomerID
        WHERE EXTRACT(MONTH FROM t.TransactionDate) = EXTRACT(MONTH FROM SYSDATE)
          AND EXTRACT(YEAR FROM t.TransactionDate) = EXTRACT(YEAR FROM SYSDATE);

    v_last_customer VARCHAR2(100) := '';
BEGIN
    FOR rec IN GenerateMonthlyStatements LOOP
        IF rec.Name != v_last_customer THEN
            DBMS_OUTPUT.PUT_LINE('--- Statement for: ' || rec.Name || ' ---');
            v_last_customer := rec.Name;
        END IF;

        DBMS_OUTPUT.PUT_LINE(
            '  TxnID: ' || rec.TransactionID ||
            '  Date: ' || TO_CHAR(rec.TransactionDate, 'DD-MON-YYYY') ||
            '  Type: ' || rec.TransactionType ||
            '  Amount: ' || rec.Amount
        );
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Monthly statements printed.');
END;
/
