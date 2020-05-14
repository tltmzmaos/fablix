delimiter $$

create procedure add_movie(IN movie_id VARCHAR(10), IN movie_title VARCHAR(100), IN movie_year INT, IN movie_director VARCHAR(100),
IN star_id VARCHAR(10) ,IN star_name VARCHAR(100), IN genre_name VARCHAR(32) )
BEGIN

	IF ((select count(*) from movies where movies.title = movie_title and
    movies.director = movie_director and movies.year = movie_year) = 0) THEN

        insert into movies values (movie_id, movie_title,movie_year, movie_director);
        select "movie inserted" as answer;
        IF ((select count(*) from genres where genres.name = genre_name ) = 1) THEN

            insert into genres_in_movies (genreId,movieId) values(
			(select genres.id
			from genres where genres.name = genre_name), movie_id);

            select "genre exists" as answer;
        ELSE
			SELECT "genre doesn't exist" as answer;
			insert into genres values (NULL, genre_name);

            insert into genres_in_movies (genreId,movieId) values(
			(select genres.id
			from genres where genres.name = genre_name), movie_id);

			SELECT "added genres and linked to movies" as answer;

		END IF;

        IF ((select count(*) from stars where stars.name = star_name) = 1) THEN
			SELECT "star exists" as answer;
			insert into stars_in_movies (starId, movieId) values (
            (select stars.id
            from stars where stars.name = star_name), movie_id);
            SELECT "adding stars" as answer;

        ELSE
			SELECT "star doesn't exist"  as answer;
			insert into stars values (star_id, star_name, NULL);
			insert into stars_in_movies (starId, movieId) values (
            star_id, movie_id);
			SELECT "adding stars" as answer;
        END IF;

    ELSE
		SELECT "movie already in database" as answer;
	END IF;

END
$$

