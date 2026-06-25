-- Scenario 3: Block invalid withdrawals and negative deposits

CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_balance NUMBER;
BEGIN
    IF :NEW.TransactionType = 'Deposit' AND :NEW.Amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Deposit amount must be greater than zero.');
    END IF;

    IF :NEW.TransactionType = 'Withdrawal' THEN
        SELECT Balance INTO v_balance
        FROM Accounts
        WHERE AccountID = :NEW.AccountID;

        IF v_balance < :NEW.Amount THEN
            RAISE_APPLICATION_ERROR(-20011, 'Withdrawal denied: insufficient balance.');
        END IF;
    END IF;
END;
/

-- test valid deposit
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (4, 1, SYSDATE, 300, 'Deposit');

-- test bad withdrawal (should fail)
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (5, 2, SYSDATE, 999999, 'Withdrawal');
