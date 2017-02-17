CREATE TABLE production_result
(
  production_actual DATETIME,
  SCHEDULE_ID       BIGINT(20) PRIMARY KEY NOT NULL,
  CONSTRAINT FK_production_result_SCHEDULE_ID FOREIGN KEY (SCHEDULE_ID) REFERENCES production_schedule (ID)
);
CREATE TABLE production_schedule
(
  ID                  BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  product_name        VARCHAR(12),
  production_expected DATETIME
);
CREATE UNIQUE INDEX UNQ_production_schedule_0
  ON production_schedule (product_name);
