-- Scenario 1: Auto-update LastModified on customer update

CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    :NEW.LastModified := SYSDATE;
END;
/

-- test
UPDATE Customers SET Balance = 1100 WHERE CustomerID = 1;
SELECT CustomerID, Name, Balance, LastModified FROM Customers WHERE CustomerID = 1;
