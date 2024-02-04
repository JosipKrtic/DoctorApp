CREATE NONCLUSTERED INDEX IX_MedicationWithGuide 
ON Medication (guide)
WHERE guide IS NOT NULL;

CREATE UNIQUE NONCLUSTERED INDEX IX_UniqueMedicationName
ON Medication(name);

ALTER TABLE Patient
ADD CONSTRAINT Positive_Patient_Age
CHECK (age > 0);