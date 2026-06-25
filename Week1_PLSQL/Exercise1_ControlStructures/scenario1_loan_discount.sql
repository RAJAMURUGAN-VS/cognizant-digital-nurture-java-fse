-- Scenario 1: Apply 1% discount on loan interest rate for customers above 60

DECLARE
    CURSOR c_customers IS
        SELECT CustomerID, Name, DOB FROM Customers;

    v_age NUMBER;
BEGIN
    FOR rec IN c_customers LOOP
        v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, rec.DOB) / 12);

        IF v_age > 60 THEN
            UPDATE Loans
            SET InterestRate = InterestRate - 1
            WHERE CustomerID = rec.CustomerID
              AND InterestRate > 1;

            DBMS_OUTPUT.PUT_LINE('Discount applied for: ' || rec.Name || ' (Age: ' || v_age || ')');
        END IF;
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Done processing loan discounts.');
END;
/
