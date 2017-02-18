CREATE TABLE artist
(
  ID        BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  available DATE                   NOT NULL,
  name      VARCHAR(64)            NOT NULL
);
CREATE UNIQUE INDEX name
  ON artist (name);
CREATE TABLE artist_dispatch_contract
(
  ID            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  customer_name VARCHAR(32)            NOT NULL,
  DATE          DATE                   NOT NULL,
  ARTIST_ID     BIGINT(20),
  CONSTRAINT FK_artist_dispatch_contract_ARTIST_ID FOREIGN KEY (ARTIST_ID) REFERENCES artist (ID)
);
CREATE INDEX FK_artist_dispatch_contract_ARTIST_ID
  ON artist_dispatch_contract (ARTIST_ID);
