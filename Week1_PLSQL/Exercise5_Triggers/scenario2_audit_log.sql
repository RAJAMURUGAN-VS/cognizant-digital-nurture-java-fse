-- Scenario 2: Log every transaction insert into AuditLog

CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TransactionID, AccountID, TransactionDate, Amount, TransactionType, LoggedAt)
    VALUES (:NEW.TransactionID, :NEW.AccountID, :NEW.TransactionDate, :NEW.Amount, :NEW.TransactionType, SYSDATE);
END;
/

-- test
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (3, 1, SYSDATE, 500, 'Deposit');

SELECT * FROM AuditLog;
