USE [DoctorApp]
CREATE LOGIN [Login2] WITH PASSWORD='12345', DEFAULT_DATABASE=[DoctorApp]
DENY VIEW ANY DATABASE TO [Login2]
GO

CREATE USER [UserWithLogin] FOR LOGIN [Login2] -- UserWithLogin povezan s prijavnim nalogom

CREATE USER [UserWithoutLogin] WITHOUT LOGIN -- UserWithoutLogin bez prijavnog naloga!

-- UserWithoutLogin ima ovlasti za �itanje podataka iz tablice!
GRANT SELECT ON [dbo].[Doctor] TO [UserWithoutLogin]

-- UserWithLogin mo�e impersonalizirati korisnika UserWithoutLogin
GRANT IMPERSONATE ON USER::UserWithoutLogin to UserWithLogin

USE DoctorApp
EXECUTE AS USER = 'UserWithoutLogin' -- Izvr�i upite u sigurnosnom kontekstu korisnika UserWithoutLogin
SELECT * FROM Doctor -- Uspje�no izvr�en upit!
