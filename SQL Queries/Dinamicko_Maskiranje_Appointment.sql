ALTER TABLE Appointment
ALTER COLUMN duration ADD MASKED WITH(FUNCTION = 'random(0, 30)')
USE DoctorApp
CREATE USER [Korisnik2] WITHOUT LOGIN
GRANT SELECT ON [dbo].[Appointment] TO [Korisnik2]
GO

-- Izvr�i upit u sigurnosnom kontekstu kreiranog korisnika 
EXECUTE AS USER = 'Korisnik2'
SELECT * FROM Appointment
REVERT