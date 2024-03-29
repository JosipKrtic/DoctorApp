USE [DoctorApp]
GO
/****** Object:  StoredProcedure [dbo].[sifriraj]    Script Date: 24.1.2024. 20:21:18 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Encryption Procedure
ALTER PROCEDURE [dbo].[sifriraj]
    @OIB INT,
    @password VARCHAR(50),
    @firstName VARCHAR(50),
    @lastName VARCHAR(50),
    @age INT,
    @sex VARCHAR(10),
    @address VARCHAR(100),
    @contact VARCHAR(20),
    @bloodType VARCHAR(10),
    @weight INT,
    @height INT,
    @medicalRecordID INT,
    @insuranceID INT,
    @doctorID INT
AS
BEGIN
    OPEN SYMMETRIC KEY MojSimetricniKljuc
    DECRYPTION BY ASYMMETRIC KEY MojAsimetricniKljuc

	INSERT INTO Patient(OIB, password, firstName, lastName, age, sex, address, contact, bloodType, weight, height, medicalRecordID, insuranceID, doctorID)
    VALUES (@OIB, ENCRYPTBYKEY(KEY_GUID('MojSimetricniKljuc'), @password), @firstName, @lastName, @age, @sex, @address, @contact, @bloodType, @weight, @height, @medicalRecordID, @insuranceID, @doctorID);
END;