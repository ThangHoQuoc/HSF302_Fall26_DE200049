-- Chạy trước Hibernate. Các batch dùng GO để hỗ trợ cả Spring SQL init và SSMS.
SET XACT_ABORT ON;
BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.Majors', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Majors (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        code VARCHAR(2) NOT NULL CONSTRAINT UQ_Majors_Code UNIQUE,
        name NVARCHAR(100) NOT NULL,
        name_en NVARCHAR(100) NOT NULL
    );
END;
GO

INSERT INTO dbo.Majors (code, name, name_en)
SELECT seed.code, seed.name, seed.name_en
FROM (VALUES
    ('SE', N'Kỹ thuật phần mềm', N'Software Engineering'),
    ('AI', N'Trí tuệ nhân tạo', N'Artificial Intelligence'),
    ('IA', N'An toàn thông tin', N'Information Assurance'),
    ('GD', N'Thiết kế đồ họa', N'Graphic Design')
) AS seed(code, name, name_en)
WHERE NOT EXISTS (SELECT 1 FROM dbo.Majors m WHERE m.code = seed.code);
GO

-- Giữ cả mã ngành ngoài dữ liệu mẫu nếu database cũ có sử dụng.
IF COL_LENGTH(N'dbo.Students', N'major') IS NOT NULL
BEGIN
    EXEC sys.sp_executesql N'
        INSERT INTO dbo.Majors (code, name, name_en)
        SELECT DISTINCT LTRIM(RTRIM(s.major)), LTRIM(RTRIM(s.major)), LTRIM(RTRIM(s.major))
        FROM dbo.Students s
        WHERE s.major IS NOT NULL AND LTRIM(RTRIM(s.major)) <> ''''
          AND NOT EXISTS (SELECT 1 FROM dbo.Majors m WHERE m.code = LTRIM(RTRIM(s.major)));';
END;
GO

IF OBJECT_ID(N'dbo.Students', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.Students', N'major_id') IS NULL
BEGIN
    ALTER TABLE dbo.Students ADD major_id BIGINT NULL;
END;
GO

IF COL_LENGTH(N'dbo.Students', N'major') IS NOT NULL
BEGIN
    EXEC sys.sp_executesql N'
        UPDATE s SET major_id = m.id
        FROM dbo.Students s
        INNER JOIN dbo.Majors m ON m.code = LTRIM(RTRIM(s.major))
        WHERE s.major_id IS NULL;
        ALTER TABLE dbo.Students ALTER COLUMN major VARCHAR(2) NULL;';
END;
GO

IF OBJECT_ID(N'dbo.Students', N'U') IS NOT NULL
BEGIN
    EXEC sys.sp_executesql N'
        IF EXISTS (SELECT 1 FROM dbo.Students WHERE major_id IS NULL)
            THROW 50001, ''Cannot migrate Students: a student has no valid major.'', 1;
        ALTER TABLE dbo.Students ALTER COLUMN major_id BIGINT NOT NULL;';

    IF NOT EXISTS (
        SELECT 1 FROM sys.foreign_keys
        WHERE name = N'FK_Students_Majors' AND parent_object_id = OBJECT_ID(N'dbo.Students')
    )
    BEGIN
        ALTER TABLE dbo.Students WITH CHECK
            ADD CONSTRAINT FK_Students_Majors FOREIGN KEY (major_id) REFERENCES dbo.Majors(id);
    END;
END;
GO

COMMIT TRANSACTION;
GO
