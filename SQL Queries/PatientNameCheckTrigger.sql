USE [DoctorApp]
GO
/****** Object:  Trigger [dbo].[PatientNameCheck]    Script Date: 24.1.2024. 20:25:56 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [dbo].[PatientNameCheck]
ON [dbo].[Patient]
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Check the format for each newly inserted row
    IF EXISTS (
        SELECT 1
        FROM inserted
        WHERE 
			(LEFT(firstName COLLATE Latin1_General_CS_AS, 1) = LOWER(LEFT(firstName COLLATE Latin1_General_CS_AS, 1)))
			OR
			(LEFT(lastName COLLATE Latin1_General_CS_AS, 1) = LOWER(LEFT(lastName COLLATE Latin1_General_CS_AS, 1)))
    )
    BEGIN
        RAISERROR ('Invalid name format. Names must start with a capital letter followed by lowercase letters.', 16, 1);
        ROLLBACK TRANSACTION; -- Rollback the transaction to prevent the invalid data from being inserted
    END;
END;

