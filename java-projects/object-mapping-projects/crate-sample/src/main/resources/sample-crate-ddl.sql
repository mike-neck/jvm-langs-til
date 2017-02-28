CREATE TABLE student (
  id             long PRIMARY KEY,
  last_name      string NOT NULL INDEX USING PLAIN,
  first_name     string NOT NULL INDEX USING PLAIN,
  admission_year INT    NOT NULL
)
WITH (number_of_replicas = 2, column_policy = 'strict'
);
