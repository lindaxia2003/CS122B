DROP DATABASE moviedb;
CREATE DATABASE moviedb;
use moviedb;
CREATE TABLE movies(
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(100) NOT NULL default "",
	year INT NOT NULL,
	director VARCHAR(100) NOT NULL default "",
	banner_url VARCHAR(200) default NULL,
	trailer_url VARCHAR(200) default NULL,
	PRIMARY KEY (id)

);

CREATE TABLE stars(
	id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL default "",
	last_name VARCHAR(50) NOT NULL default "",
	dob DATE default NULL,
	photo_url VARCHAR(200) default NULL,
	PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies(
	star_id INT NOT NULL,
	movie_id INT NOT NULL,
	CONSTRAINT fk_starsinmovies1 FOREIGN KEY(star_id) REFERENCES stars(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_starsinmovies2 FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE genres(
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(32) NOT NULL default "",
	PRIMARY KEY (id)
);

CREATE TABLE genres_in_movies(
	genre_id INT NOT NULL,
	movie_id INT NOT NULL,
	CONSTRAINT fk_genresinmovies1 FOREIGN KEY(genre_id) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_genresinmovies2 FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE creditcards(
	id VARCHAR(20) NOT NULL default "",
	first_name VARCHAR(50) NOT NULL default "",
	last_name VARCHAR(50) NOT NULL default "",
	expiration DATE NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE customers(
	id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL default "",
	last_name VARCHAR(50) NOT NULL default "",
	cc_id VARCHAR(20) NOT NULL,
	address VARCHAR(200) NOT NULL default "",
	email VARCHAR(50) NOT NULL default "",
	password VARCHAR(20) NOT NULL default "",
	CONSTRAINT fk_customerscreditcards FOREIGN KEY(cc_id) REFERENCES creditcards(id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY(id)
);

CREATE TABLE sales(
	id INT NOT NULL AUTO_INCREMENT,
	customer_id INT NOT NULL,
	movie_id INT NOT NULL,
	sale_date TIMESTAMP NOT NULL default NOW(),
	CONSTRAINT fk_salescustomers FOREIGN KEY(customer_id) REFERENCES customers(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_salesmovies FOREIGN KEY(movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY(id)
);

CREATE TABLE shoppingcart(
	customer_id INT NOT NULL,
	movie_id INT NOT NULL,
	movie_title VARCHAR(100) NOT NULL default "",
	quantity INT NOT NULL
);

CREATE TABLE employees(
	email VARCHAR(50),
	password VARCHAR(50) NOT NULL,
	fullname VARCHAR(100),
	PRIMARY KEY (email)
);
