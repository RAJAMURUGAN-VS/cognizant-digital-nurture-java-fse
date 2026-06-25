-- Scenario 2: Mark customers as VIP if balance is over $10,000

DECLARE
    CURSOR c_customers IS
        SELECT CustomerID, Name, Balance FROM Customers;
BEGIN
    FOR rec IN c_customers LOOP
        IF rec.Balance > 10000 THEN
            UPDATE Customers
            SET IsVIP = 'TRUE'
            WHERE CustomerID = rec.CustomerID;

            DBMS_OUTPUT.PUT_LINE(rec.Name || ' marked as VIP (Balance: ' || rec.Balance || ')');
        ELSE
            DBMS_OUTPUT.PUT_LINE(rec.Name || ' is not VIP (Balance: ' || rec.Balance || ')');
        END IF;
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('VIP status update complete.');
END;
/
