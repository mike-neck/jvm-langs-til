CREATE TABLE student (
  id             long PRIMARY KEY,
  last_name      string NOT NULL INDEX USING PLAIN,
  first_name     string NOT NULL INDEX USING PLAIN,
  admission_year INT    NOT NULL
)
WITH (number_of_replicas = 2, column_policy = 'strict'
);

CREATE TABLE books (
  id     long PRIMARY KEY,
  title  string NOT NULL INDEX USING PLAIN,
  author string NOT NULL INDEX USING PLAIN
) WITH (number_of_replicas = 2, column_policy = 'strict'
);

CREATE TABLE rental (
  id         long PRIMARY KEY,
  student_id long,
  book_id    long,
  period     object ( STRICT ) AS (
  since TIMESTAMP NOT NULL,
  to_be_returnes TIMESTAMP NOT NULL,
  returned TIMESTAMP
  )
) WITH (number_of_replicas = 2, column_policy = 'strict'
);
