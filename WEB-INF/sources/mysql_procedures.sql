 -- change delimeter to //
 DELIMITER //
 CREATE PROCEDURE add_movie(m_title VARCHAR(100), m_year INT(11), m_director VARCHAR(100), star_fn VARCHAR(50), star_ln VARCHAR(50), genre_name VARCHAR(32))
 BEGIN
    DECLARE starID INT(11) DEFAULT NULL;
    DECLARE genreID INT(11) DEFAULT NULL;
    DECLARE movieID INT(11) DEFAULT NULL;

    SET starID = (SELECT id FROM stars WHERE first_name = star_fn AND last_name = star_ln);
    SET genreID = (SELECT id FROM genres WHERE name = genre_name);
    SET movieID = (SELECT id FROM movies WHERE title = m_title AND year = m_year AND director = m_director);

    -- if movie does not exist, create it
    IF movieID IS NULL THEN
        INSERT INTO movies (title, year, director) VALUES(m_title, m_year, m_director);
        SET movieID = (SELECT id FROM movies WHERE title = m_title AND year = m_year AND director = m_director);
    END IF;

    -- if star exists, link it to movie
    -- else, create star then link it to movie
    IF starID IS NOT NULL THEN 
        INSERT INTO stars_in_movies VALUES(starID, movieID);
    ELSE
        INSERT INTO stars (first_name, last_name) VALUES(star_fn, star_ln);
        SET starID = (SELECT id FROM stars WHERE first_name = star_fn AND last_name = star_ln);
        INSERT INTO stars_in_movies VALUES(starID, movieID);
    END IF;

    -- if genre exists, link it to movie
    -- else, create genre then link it to movie
    IF genreID IS NOT NULL THEN
        INSERT INTO genres_in_movies VALUES(genreID, movieID);
    ELSE
        INSERT INTO genres (name) VALUES(genre_name);
        SET genreID = (SELECT id FROM genres WHERE name = genre_name);
        INSERT INTO genres_in_movies VALUES(genreID, movieID);
    END IF;
END
//

-- change delimeter to ;
DELIMITER ;