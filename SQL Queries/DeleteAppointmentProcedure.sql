USE [DoctorApp]
GO
/****** Object:  StoredProcedure [dbo].[deleteAppointment]    Script Date: 24.1.2024. 20:23:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Procedure for number of appointments
ALTER PROCEDURE [dbo].[deleteAppointment]
    @appointmentID INT
AS
BEGIN
    DELETE FROM Appointment 
	WHERE ID = @appointmentID;
END;