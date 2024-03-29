USE [DoctorApp]
GO
/****** Object:  StoredProcedure [dbo].[updateTimeForAppointment]    Script Date: 24.1.2024. 20:23:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Procedure for number of appointments
ALTER PROCEDURE [dbo].[updateTimeForAppointment]
    @newTime TIME,
	@appointmentID INT
AS
BEGIN
    UPDATE Appointment 
	SET time = @newTime
	WHERE ID = @appointmentID;
END;
