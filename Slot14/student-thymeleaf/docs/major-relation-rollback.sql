-- Chỉ chạy trong SSMS khi quay lại phiên bản trước Phần G - Bài 9, sau khi dừng app.
-- Giữ nguyên bảng Majors và dữ liệu quan hệ; khôi phục cột mã ngành cho code cũ.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
GO

IF COL_LENGTH(N'dbo.Students', N'major') IS NULL
    ALTER TABLE dbo.Students ADD major VARCHAR(2) NULL;
GO

UPDATE s SET major = m.code
FROM dbo.Students s INNER JOIN dbo.Majors m ON m.id = s.major_id;
ALTER TABLE dbo.Students ALTER COLUMN major VARCHAR(2) NOT NULL;
ALTER TABLE dbo.Students ALTER COLUMN major_id BIGINT NULL;
GO

COMMIT TRANSACTION;
GO
