USE [DoctorApp]
GO
/****** Object:  StoredProcedure [dbo].[GetNumOfPatients]    Script Date: 24.1.2024. 20:23:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Procedure for number of appointments
ALTER PROCEDURE [dbo].[GetNumOfPatients]
    @doctorID INT,
	@numOfPatients INTEGER OUTPUT,
	@numOfPossiblePatients INTEGER OUTPUT
AS
BEGIN
    SELECT @numOfPatients = COUNT(DISTINCT OIB) 
	FROM Patient 
	WHERE doctorID = @doctorID;

	SET @numOfPossiblePatients = dbo.GetNumOfPossiblePatients(@numOfPatients);
END;