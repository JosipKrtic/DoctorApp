ALTER TABLE Medication
ALTER COLUMN name ADD MASKED WITH(FUNCTION = 'default()')-- Kreiranje korisnika koji ima ovlasti za dohvat podataka
USE DoctorApp
CREATE USER [Korisnik1] WITHOUT LOGIN
GRANT SELECT ON [dbo].[Medication] TO [Korisnik1]
GO

-- Izvrši upit u sigurnosnom kontekstu kreiranog korisnika 
EXECUTE AS USER = 'Korisnik1'
SELECT * FROM Medication
REVERT