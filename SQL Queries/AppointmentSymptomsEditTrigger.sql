USE [DoctorApp]
GO
/****** Object:  Trigger [dbo].[AppointmentSymptomsEdit]    Script Date: 24.1.2024. 20:26:46 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [dbo].[AppointmentSymptomsEdit] 
ON [dbo].[Appointment] 
AFTER INSERT
AS
BEGIN
    -- get Appointment ID
    DECLARE @ID INT = (SELECT ID FROM Inserted)
    -- get symptoms
    DECLARE @symptoms NVARCHAR(50) = (SELECT symptoms FROM inserted)

    UPDATE Appointment SET
    -- Prvi znak simptoma pretvori u veliko slovo, a ostale znakove u mala slova i dodaj "!" na kraj
    symptoms = UPPER(LEFT(@symptoms, 1)) + LOWER(SUBSTRING(@symptoms, 2, LEN(@symptoms))) + '!'
    WHERE ID = @ID
END