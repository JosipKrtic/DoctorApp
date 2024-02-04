--Creating DDL trigger on Database level
CREATE TRIGGER NoDeletingOrAlteringInsuranceTable
ON DATABASE
AFTER DROP_TABLE, ALTER_TABLE 
AS
BEGIN
 PRINT 'You cant delete or alter this table!'
 ROLLBACK
END


-- To disable trigger
DISABLE TRIGGER NoDeletingOrAlteringInsuranceTable ON DATABASE;


-- To enable trigger
ENABLE TRIGGER NoDeletingOrAlteringInsuranceTable ON DATABASE;