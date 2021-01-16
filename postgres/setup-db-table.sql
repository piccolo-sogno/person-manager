DROP DATABASE IF EXISTS sample;

CREATE DATABASE sample;

\c sample

DROP TABLE IF EXISTS persons CASCADE;

CREATE TABLE persons (
	id serial PRIMARY KEY,
	name VARCHAR ( 50 ) NOT NULL,
	surname VARCHAR ( 50 ) NOT NULL,
	created_on TIMESTAMP NOT NULL,
	updated_on TIMESTAMP NOT NULL
);
