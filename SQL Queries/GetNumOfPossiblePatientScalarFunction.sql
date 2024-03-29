USE [DoctorApp]
GO
/****** Object:  UserDefinedFunction [dbo].[GetNumOfPossiblePatients]    Script Date: 24.1.2024. 20:23:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER FUNCTION [dbo].[GetNumOfPossiblePatients] (@numOfPatients INT)
RETURNS INT
AS
BEGIN
    --Calculates the remaining possible number of patients for current doctor
    RETURN  50 - @numOfPatients;
END;