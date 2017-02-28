INSERT INTO student (id, last_name, first_name, admission_year) VALUES (1, '山田', '太郎', 2016);
INSERT INTO student (id, last_name, first_name, admission_year) VALUES (2, '山田', '華子', 2015);
INSERT INTO student (id, last_name, first_name, admission_year) VALUES (3, '高田', '純次', 2017);

SELECT *
FROM student;


INSERT INTO books (id, title, author) VALUES (1, 'ゴリオ爺さん', 'バルザック');
INSERT INTO books (id, title, author) VALUES (2, '赤と黒', 'スタンダール');
INSERT INTO books (id, title, author) VALUES (3, '異邦人', 'カミュ');
INSERT INTO books (id, title, author) VALUES (4, '失われた時を求めて', 'プルースト');
INSERT INTO books (id, title, author) VALUES (5, '惡の華', 'ボードレール');

INSERT INTO rental (id, student_id, book_id, period)
VALUES (1, 1, 3, { since = '2017-02-21T04:00:00Z', to_be_returnes = '2017-02-28T04:00:00Z' });
INSERT INTO rental (id, student_id, book_id, period) VALUES
  (2, 1, 5, { since = '2017-02-21T04:00:00Z', to_be_returnes = '2017-02-28T04:00:00Z',
   returned = '2017-02-26T07:00:00Z' });
INSERT INTO rental (id, student_id, book_id, period)
VALUES (3, 2, 1, { since = '2017-02-22T04:00:00Z', to_be_returnes = '2017-03-01T01:20:00Z' });

SELECT
  s.last_name,
  s.first_name,
  b.title,
  date_format('%Y-%m-%dT%H:%i:%s.%fZ', r.period ['since']),
  r.period ['returned']
FROM
  student s
  JOIN rental r ON s.id = r.student_id
  JOIN books b ON r.book_id = b.id;

UPDATE rental
SET period ['returned'] = '2017-02-28T08:30:00Z'
WHERE id = 3;
