IF DB_ID('dtm_busi') IS NULL
BEGIN
    CREATE DATABASE dtm_busi;
END
GO

USE dtm_busi;
GO

IF OBJECT_ID('dbo.user_account', 'U') IS NOT NULL
    DROP TABLE dbo.user_account;
GO

CREATE TABLE dbo.user_account (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0,
    trading_balance DECIMAL(10, 2) NOT NULL DEFAULT 0,
    create_time DATETIME2 DEFAULT GETDATE(),
    update_time DATETIME2 DEFAULT GETDATE()
);
GO

CREATE NONCLUSTERED INDEX IX_user_account_create_time ON dbo.user_account (create_time);
CREATE NONCLUSTERED INDEX IX_user_account_update_time ON dbo.user_account (update_time);
GO

MERGE dbo.user_account AS target
USING (VALUES (1, 10000), (2, 10000)) AS source (user_id, balance)
ON target.user_id = source.user_id
WHEN MATCHED THEN
    UPDATE SET balance = source.balance
WHEN NOT MATCHED THEN
    INSERT (user_id, balance)
    VALUES (source.user_id, source.balance);
GO