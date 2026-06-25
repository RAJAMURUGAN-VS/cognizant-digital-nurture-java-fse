-- Scenario 2: Calculate EMI for a loan
-- Formula: EMI = [P * r * (1+r)^n] / [(1+r)^n - 1]
-- P = principal, r = monthly interest rate, n = number of months

CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount   IN NUMBER,
    p_annual_rate   IN NUMBER,
    p_years         IN NUMBER
) RETURN NUMBER AS
    v_monthly_rate  NUMBER;
    v_months        NUMBER;
    v_emi           NUMBER;
BEGIN
    v_monthly_rate := p_annual_rate / (12 * 100);
    v_months       := p_years * 12;

    IF v_monthly_rate = 0 THEN
        v_emi := p_loan_amount / v_months;
    ELSE
        v_emi := p_loan_amount * v_monthly_rate * POWER(1 + v_monthly_rate, v_months)
                 / (POWER(1 + v_monthly_rate, v_months) - 1);
    END IF;

    RETURN ROUND(v_emi, 2);
END;
/

-- test
DECLARE
    v_emi NUMBER;
BEGIN
    v_emi := CalculateMonthlyInstallment(50000, 8, 3);
    DBMS_OUTPUT.PUT_LINE('Monthly installment: ' || v_emi);
END;
/
