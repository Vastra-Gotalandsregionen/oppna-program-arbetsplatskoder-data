ALTER TABLE prodn3 ALTER producentid DROP NOT NULL;
ALTER TABLE prodn1 DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE prodn2 DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE prodn3 DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE ao3 DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE archived_data DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE data DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE verksamhet DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE vardform DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE viewapkwithao3 DROP COLUMN IF EXISTS ssma_timestamp;
ALTER TABLE viewapkwithao3temp DROP COLUMN IF EXISTS ssma_timestamp;

update data set arbetsplatskodlan=trim(arbetsplatskodlan) where arbetsplatskodlan like '% %';