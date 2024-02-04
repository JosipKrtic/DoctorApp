-- Create a login for DB_Admin
CREATE LOGIN DB_Admin WITH PASSWORD = 'adminPass';

-- Switch to the target database
USE DoctorApp;

-- Create a user for DB_Admin in the database
CREATE USER DB_Admin FOR LOGIN DB_Admin;

-- Create a new database role (DB_Administratori)
CREATE ROLE DB_Administratori;

-- Grant all privileges to the DB_Administratori role
GRANT ALL TO DB_Administratori;

-- Add DB_Admin to the DB_Administratori role
ALTER ROLE DB_Administratori ADD MEMBER DB_Admin;

-- Check role memberships for DB_Admin
EXEC sp_helpuser 'DB_Admin';

-- Check permissions for DB_Administratori role
EXEC sp_helprolemember 'DB_Administratori';
