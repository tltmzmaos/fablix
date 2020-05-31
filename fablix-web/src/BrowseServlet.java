import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "BrowseServlet", urlPatterns = "/browse")
public class BrowseServlet extends HttpServlet {
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String browseInput = request.getParameter("browse").toLowerCase();
        PrintWriter out = response.getWriter();

        try{
            // Single connection
            //Connection con = dataSource.getConnection();

            // Connection pooling
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedbreadonly");
            if (ds == null)
                out.println("ds is null.");
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
            dbcon.setReadOnly(true);

            String query = "";
            if(browseInput.length() > 1){

                query = "select gs.id, gs.title, gs.year, gs.director, gs.genres, gs.genreId, gs.stars, gs.starId, rating\n" +
                        "from\n" +
                        "(select movies.id, title, director, year, group_concat(distinct genres.name) as genres,\n" +
                        "group_concat(distinct genres.id order by genres.name) as genreId,\n" +
                        "group_concat(distinct stars.name) as stars,\n" +
                        "group_concat(distinct stars.id order by stars.name) as starId\n" +
                        "from movies, stars, stars_in_movies, genres, genres_in_movies\n" +
                        "where\n" +
                        "movies.id = stars_in_movies.movieId and\n" +
                        "stars.id = stars_in_movies.starId and\n" +
                        "genres_in_movies.movieId = movies.id and\n" +
                        "genres.id = genres_in_movies.genreId and\n" +
                        "genres.name like ?\n"+
                        "group by movies.id) as gs\n" +
                        "LEFT JOIN ratings ON\n" +
                        "ratings.movieId = gs.id\n" +
                        ";";

                // Single connection
                // PreparedStatement statement = con.prepareStatement(query);

                // Connection pooling
                PreparedStatement statement = dbcon.prepareStatement(query);

                statement.setString(1, browseInput + "%");
                ResultSet rs = statement.executeQuery();

                JsonArray jsonArray = new JsonArray();
                while (rs.next()){
                    String id = rs.getString("gs.id");
                    String title = rs.getString("gs.title");
                    String year = rs.getString("gs.year");
                    String director = rs.getString("gs.director");
                    String genres = rs.getString("gs.genres");
                    String genreId = rs.getString("gs.genreId");
                    String stars = rs.getString("gs.stars");
                    String starId = rs.getString("gs.starId");
                    String rating = rs.getString("rating");

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("movie_id", id);
                    jsonObject.addProperty("movie_title", title);
                    jsonObject.addProperty("movie_year", year);
                    jsonObject.addProperty("movie_director", director);
                    jsonObject.addProperty("movie_genres", genres);
                    jsonObject.addProperty("genre_id", genreId);
                    jsonObject.addProperty("movie_stars", stars);
                    jsonObject.addProperty("star_id",starId);
                    jsonObject.addProperty("movie_rating", rating);

                    jsonArray.add(jsonObject);
                }
                out.write(jsonArray.toString());
                response.setStatus(200);
                rs.close();
                statement.close();

                // Single connection
                //con.close();

                // Connection pooling
                dbcon.close();
            }else{
                if(browseInput.equals("*")){

                    query = "select gs.id, gs.title, gs.year, gs.director, gs.genres, gs.genreId, gs.stars, gs.starId, rating\n" +
                            "from\n" +
                            "(select movies.id, title, director, year, group_concat(distinct genres.name) as genres,\n" +
                            "group_concat(distinct genres.id order by genres.name) as genreId,\n" +
                            "group_concat(distinct stars.name) as stars,\n" +
                            "group_concat(distinct stars.id order by stars.name) as starId\n" +
                            "from movies, stars, stars_in_movies, genres, genres_in_movies\n" +
                            "where\n" +
                            "movies.id = stars_in_movies.movieId and\n" +
                            "stars.id = stars_in_movies.starId and\n" +
                            "genres_in_movies.movieId = movies.id and\n" +
                            "genres.id = genres_in_movies.genreId and\n" +
                            "movies.title REGEXP '^[^A-Za-z0-9]'\n"+
                            "group by movies.id) as gs\n" +
                            "LEFT JOIN ratings ON\n" +
                            "ratings.movieId = gs.id\n" +
                            ";";

                    // Single connection
                    //PreparedStatement statement = con.prepareStatement(query);

                    // Connection pooling
                    PreparedStatement statement = dbcon.prepareStatement(query);

                    ResultSet rs = statement.executeQuery();

                    JsonArray jsonArray = new JsonArray();
                    while (rs.next()){
                        String id = rs.getString("gs.id");
                        String title = rs.getString("gs.title");
                        String year = rs.getString("gs.year");
                        String director = rs.getString("gs.director");
                        String genres = rs.getString("gs.genres");
                        String genreId = rs.getString("gs.genreId");
                        String stars = rs.getString("gs.stars");
                        String starId = rs.getString("gs.starId");
                        String rating = rs.getString("rating");

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("movie_id", id);
                        jsonObject.addProperty("movie_title", title);
                        jsonObject.addProperty("movie_year", year);
                        jsonObject.addProperty("movie_director", director);
                        jsonObject.addProperty("movie_genres", genres);
                        jsonObject.addProperty("genre_id", genreId);
                        jsonObject.addProperty("movie_stars", stars);
                        jsonObject.addProperty("star_id",starId);
                        jsonObject.addProperty("movie_rating", rating);

                        jsonArray.add(jsonObject);
                    }
                    out.write(jsonArray.toString());
                    response.setStatus(200);
                    rs.close();
                    statement.close();

                    // Single connection
                    //con.close();

                    // Connection pooling
                    dbcon.close();
                }else {

                    query = "select gs.id, gs.title, gs.year, gs.director, gs.genres, gs.genreId, gs.stars, gs.starId, rating\n" +
                            "from\n" +
                            "(select movies.id, title, director, year, group_concat(distinct genres.name) as genres,\n" +
                            "group_concat(distinct genres.id order by genres.name) as genreId,\n" +
                            "group_concat(distinct stars.name) as stars,\n" +
                            "group_concat(distinct stars.id order by stars.name) as starId\n" +
                            "from movies, stars, stars_in_movies, genres, genres_in_movies\n" +
                            "where\n" +
                            "movies.id = stars_in_movies.movieId and\n" +
                            "stars.id = stars_in_movies.starId and\n" +
                            "genres_in_movies.movieId = movies.id and\n" +
                            "genres.id = genres_in_movies.genreId and\n" +
                            "movies.title like ?\n"+
                            "group by movies.id) as gs\n" +
                            "LEFT JOIN ratings ON\n" +
                            "ratings.movieId = gs.id\n" +
                            ";";

                    // Single connection
                    // PreparedStatement statement = con.prepareStatement(query);

                    // Connection pooling
                    PreparedStatement statement = dbcon.prepareStatement(query);

                    statement.setString(1, browseInput + "%");
                    ResultSet rs = statement.executeQuery();

                    JsonArray jsonArray = new JsonArray();
                    while (rs.next()){
                        String id = rs.getString("gs.id");
                        String title = rs.getString("gs.title");
                        String year = rs.getString("gs.year");
                        String director = rs.getString("gs.director");
                        String genres = rs.getString("gs.genres");
                        String genreId = rs.getString("gs.genreId");
                        String stars = rs.getString("gs.stars");
                        String starId = rs.getString("gs.starId");
                        String rating = rs.getString("rating");

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("movie_id", id);
                        jsonObject.addProperty("movie_title", title);
                        jsonObject.addProperty("movie_year", year);
                        jsonObject.addProperty("movie_director", director);
                        jsonObject.addProperty("movie_genres", genres);
                        jsonObject.addProperty("genre_id", genreId);
                        jsonObject.addProperty("movie_stars", stars);
                        jsonObject.addProperty("star_id",starId);
                        jsonObject.addProperty("movie_rating", rating);

                        jsonArray.add(jsonObject);
                    }
                    out.write(jsonArray.toString());
                    response.setStatus(200);
                    rs.close();
                    statement.close();
                    // Single connection
                    // con.close();

                    // Connection pooling
                    dbcon.close();
                }
            }

        }catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            response.getWriter().write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
