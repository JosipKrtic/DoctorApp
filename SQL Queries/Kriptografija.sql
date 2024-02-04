-- Kreiranje glavnog kljuèa baze podataka 
CREATE MASTER KEY ENCRYPTION BY PASSWORD = 'MasterKeyPass' 
GO 


-- Kreiranje asimetriènog kljuèa šifriranog glavnim kljuèem baze podataka 
CREATE ASYMMETRIC KEY MojAsimetricniKljuc 
WITH ALGORITHM = RSA_2048 
GO 


-- Kreiranje simetriènog kljuèa šifriranog asimetriènim kljuèem 
CREATE SYMMETRIC KEY MojSimetricniKljuc 
WITH ALGORITHM = AES_256 
ENCRYPTION BY ASYMMETRIC KEY MojAsimetricniKljuc 
GO
