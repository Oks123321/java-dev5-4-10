ALTER TABLE human
DROP CONSTRAINT gender_values;

ALTER TABLE human
ADD CONSTRAINT gender_values
CHECK gender IN ('male', 'female', 'cow');