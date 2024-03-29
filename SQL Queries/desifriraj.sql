USE [DoctorApp]
GO
/****** Object:  StoredProcedure [dbo].[desifriraj]    Script Date: 24.1.2024. 20:21:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Decryption Procedure
ALTER PROCEDURE [dbo].[desifriraj]
    @OIB INT,
    @password VARCHAR(50),
	@isPasswordMatch BIT OUTPUT
AS
BEGIN
    OPEN SYMMETRIC KEY MojSimetricniKljuc
    DECRYPTION BY ASYMMETRIC KEY MojAsimetricniKljuc

	-- Dešifriranje podataka
    DECLARE @decryptedPassword VARCHAR(50);
    SELECT @decryptedPassword = CONVERT(VARCHAR(50), DECRYPTBYKEY(password))
    FROM Patient
    WHERE OIB = @OIB;

    -- Check if the decrypted password matches the provided password
    IF @decryptedPassword = @password
        SET @isPasswordMatch = 1; -- True
    ELSE
        SET @isPasswordMatch = 0; -- False
END;