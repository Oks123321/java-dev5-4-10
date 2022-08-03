UPDATE human
SET gender = 'male'
WHERE gender NOT IN ('male', 'female');

ALTER TABLE human
ADD CONSTRAINT gender_values
CHECK gender IN ('male', 'female');