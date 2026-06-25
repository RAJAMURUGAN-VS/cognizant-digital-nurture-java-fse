-- Scenario 3: Update loan interest rates based on new policy
-- New policy: loans above 10000 get 8%, others get 6%

DECLARE
    CURSOR UpdateLoanInterestRates IS
        SELECT LoanID, LoanAmount, InterestRate FROM Loans FOR UPDATE;

    v_new_rate NUMBER;
BEGIN
    FOR rec IN UpdateLoanInterestRates LOOP
        IF rec.LoanAmount > 10000 THEN
            v_new_rate := 8;
        ELSE
            v_new_rate := 6;
        END IF;

        UPDATE Loans
        SET InterestRate = v_new_rate
        WHERE LoanID = rec.LoanID;

        DBMS_OUTPUT.PUT_LINE(
            'Loan ' || rec.LoanID ||
            ' updated: old rate = ' || rec.InterestRate ||
            ', new rate = ' || v_new_rate
        );
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('All loan interest rates updated.');
END;
/
