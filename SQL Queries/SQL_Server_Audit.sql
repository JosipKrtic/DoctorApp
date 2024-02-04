---1st SQL Server Audit-- Create audit object with T-SQL
USE [master]
CREATE SERVER AUDIT [DoctorAppDDL_Audit] TO FILE(
FILEPATH = N'C:\SQLServer_Audit\'
,MAXSIZE = 0 MB
,MAX_ROLLOVER_FILES = 2147483647
,RESERVE_DISK_SPACE = OFF
)
WITH( QUEUE_DELAY = 1000
,ON_FAILURE = CONTINUE
)
ALTER SERVER AUDIT [DoctorAppDDL_Audit] WITH (STATE = OFF)

-- Create SQL Server audit specification for DDL operations
USE MASTER
CREATE SERVER AUDIT SPECIFICATION [DDL_Operations]
FOR SERVER AUDIT [DoctorAppDDL_Audit]
ADD (SCHEMA_OBJECT_CHANGE_GROUP)
WITH (STATE = OFF)

-- Activate the DDL audit object and specification
USE MASTER
-- Activate the server audit object
ALTER SERVER AUDIT [DoctorAppDDL_Audit] WITH (STATE=ON)
-- Activate the server audit specification for DDL operations
ALTER SERVER AUDIT SPECIFICATION [DDL_Operations] WITH (STATE=ON)


-- Alter the table to allow NULLs for the Height column
ALTER TABLE Patient
ALTER COLUMN Height INT NULL;


---2nd SQL Server Audit
-- Create audit object with T-SQL
USE [master]
CREATE SERVER AUDIT [DoctorAppNadgledanje] TO FILE(
FILEPATH = N'C:\SQLServer_Audit\'
,MAXSIZE = 0 MB
,MAX_ROLLOVER_FILES = 2147483647
,RESERVE_DISK_SPACE = OFF
)
WITH( QUEUE_DELAY = 1000
,ON_FAILURE = CONTINUE
)
ALTER SERVER AUDIT [DoctorAppNadgledanje] WITH (STATE = OFF)

-- Create SQL Server audit specification
USE MASTER
CREATE SERVER AUDIT SPECIFICATION [Failed_Login]
FOR SERVER AUDIT [DoctorAppNadgledanje]
ADD (FAILED_LOGIN_GROUP)
WITH (STATE = OFF)


-- Activate the audit object and specification
USE MASTER
-- Activate the server audit object
ALTER SERVER AUDIT [DoctorAppNadgledanje] WITH (STATE=ON)
-- Activate the server audit specification
ALTER SERVER AUDIT SPECIFICATION [Failed_Login] WITH (STATE=ON)
