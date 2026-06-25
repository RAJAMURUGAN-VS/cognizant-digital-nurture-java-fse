-- Scenario 3: Print reminders for loans due in the next 30 days

DECLARE
    CURSOR c_due_loans IS
        SELECT l.LoanID, l.EndDate, c.Name
        FROM Loans l
        JOIN Customers c ON l.CustomerID = c.CustomerID
        WHERE l.EndDate BETWEEN SYSDATE AND SYSDATE + 30;
BEGIN
    FOR rec IN c_due_loans LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Reminder: Dear ' || rec.Name ||
            ', your loan (ID: ' || rec.LoanID || ') is due on ' ||
            TO_CHAR(rec.EndDate, 'DD-MON-YYYY') ||
            '. Please make sure you have enough balance.'
        );
    END LOOP;

    IF SQL%NOTFOUND THEN
        DBMS_OUTPUT.PUT_LINE('No loans due in the next 30 days.');
    END IF;
END;
/
