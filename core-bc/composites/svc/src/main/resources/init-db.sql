ALTER TABLE prodn3 ALTER producentid DROP NOT NULL;
--ALTER TABLE prodn1 DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE prodn2 DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE prodn3 DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE ao3 DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE archived_data DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE data DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE verksamhet DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE vardform DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE viewapkwithao3 DROP COLUMN IF EXISTS ssma_timestamp;
--ALTER TABLE viewapkwithao3temp DROP COLUMN IF EXISTS ssma_timestamp;

--ALTER TABLE fakturering ADD COLUMN IF NOT EXISTS fakturering_kort_text text;
update fakturering set fakturering_kort_text = 'Fakt VGR' where faktureringtext = 'Faktureras  VGR ';
update fakturering set fakturering_kort_text = 'Fakt ej VGR' where faktureringtext = 'Faktureras inte VGR';
update fakturering set fakturering_kort_text = 'Enl regler' where faktureringtext = 'Varierar';

update data set arbetsplatskodlan=trim(arbetsplatskodlan) where arbetsplatskodlan like '% %';