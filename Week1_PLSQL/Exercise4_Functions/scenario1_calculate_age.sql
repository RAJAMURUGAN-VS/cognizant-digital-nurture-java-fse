-- Scenario 1: Calculate customer age from DOB

CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob IN DATE
) RETURN NUMBER AS
    v_age NUMBER;
BEGIN
    v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    RETURN v_age;
END;
/

-- test it
DECLARE
    v_age NUMBER;
BEGIN
    SELECT CalculateAge(DOB) INTO v_age FROM Customers WHERE CustomerID = 1;
    DBMS_OUTPUT.PUT_LINE('Age of customer 1: ' || v_age);

    SELECT CalculateAge(DOB) INTO v_age FROM Customers WHERE CustomerID = 3;
    DBMS_OUTPUT.PUT_LINE('Age of customer 3: ' || v_age);
END;
/
