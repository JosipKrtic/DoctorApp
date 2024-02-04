BACKUP DATABASE [DoctorApp]
TO DISK = N'C:\DoctorAppBackup\DoctorAppBackup.bak' WITH NOFORMAT, NOINIT, NAME = N'DoctorApp Full Database Backup', SKIP, NOREWIND, NOUNLOAD, STATS = 10;


BACKUP DATABASE [DoctorApp] 
TO DISK = N'C:\DoctorAppBackup\Diferencijalna.bak' WITH DIFFERENTIAL


BACKUP LOG [DoctorApp]
TO DISK = N'C:\DoctorAppBackup\transakcijskiLog1.bak'
WITH NAME = 'transakcijskiLog1';


BACKUP LOG [DoctorApp]
TO DISK = N'C:\DoctorAppBackup\transakcijskiLog2.bak'
WITH NAME = 'transakcijskiLog2';



-- Launch app, add patient, appointment...



--full backup
USE [master]
ALTER DATABASE [DoctorApp] SET SINGLE_USER WITH ROLLBACK IMMEDIATE

RESTORE DATABASE [DoctorApp] 
FROM DISK = N'C:\DoctorAppBackup\DoctorAppBackup.bak' WITH FILE = 1, NORECOVERY, 
NOUNLOAD, REPLACE, STATS = 5

RESTORE DATABASE [DoctorApp] 
FROM DISK = N'C:\DoctorAppBackup\Diferencijalna.bak' WITH FILE = 1, NORECOVERY, 
NOUNLOAD, STATS = 5

RESTORE LOG [DoctorApp] 
FROM DISK =  N'C:\DoctorAppBackup\transakcijskiLog1.bak' WITH FILE = 1, NORECOVERY, 
NOUNLOAD, STATS = 5

RESTORE LOG [DoctorApp] 
FROM DISK = N'C:\DoctorAppBackup\transakcijskiLog2.bak' WITH FILE = 1, NOUNLOAD, STATS = 5

ALTER DATABASE [DoctorApp] SET MULTI_USER
