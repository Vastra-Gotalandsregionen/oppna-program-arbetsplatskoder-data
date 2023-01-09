ALTER TABLE data ADD COLUMN "ao3_old" text;
UPDATE data SET ao3_old = ao3 where ao3_old is null;

ALTER TABLE data ADD COLUMN "ansvar_old" text;
UPDATE data SET ansvar_old = ansvar where ansvar_old is null;

ALTER TABLE data ADD COLUMN "frivillig_uppgift_old" text;
UPDATE data SET frivillig_uppgift_old = frivillig_uppgift where frivillig_uppgift_old is null;
