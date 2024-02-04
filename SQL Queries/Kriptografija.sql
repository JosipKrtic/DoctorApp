-- Kreiranje glavnog klju�a baze podataka 
CREATE MASTER KEY ENCRYPTION BY PASSWORD = 'MasterKeyPass' 
GO 


-- Kreiranje asimetri�nog klju�a �ifriranog glavnim klju�em baze podataka 
CREATE ASYMMETRIC KEY MojAsimetricniKljuc 
WITH ALGORITHM = RSA_2048 
GO 


-- Kreiranje simetri�nog klju�a �ifriranog asimetri�nim klju�em 
CREATE SYMMETRIC KEY MojSimetricniKljuc 
WITH ALGORITHM = AES_256 
ENCRYPTION BY ASYMMETRIC KEY MojAsimetricniKljuc 
GO
