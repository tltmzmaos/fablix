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


@WebServlet(name = "SingleMovieServlet", urlPatterns = "/single-movie")
public class SingleMovieServlet extends HttpServlet{
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String t = request.getParameter("id");

        try{
            // Single connection
            // Connection con = dataSource.getConnection();

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

            String query = "select mr.id, mr.title, mr.year, mr.director, mr.genre, mr.genreId, star.starId, star.starName, mr.rating from\n" +
                    "(select m.id, m.title, m.year, m.director, m.genre, m.genreId, rating from\n" +
                    "(select movies.id, movies.title, movies.year, movies.director, group_concat(distinct genres.name) as genre, group_concat(distinct genres.id order by genres.name) as genreId\n" +
                    "from movies, genres, genres_in_movies\n" +
                    "where movies.id = genres_in_movies.movieId and\n" +
                    "genres_in_movies.genreId = genres.id and\n" +
                    "movies.id = ?) as m\n" +
                    "LEFT JOIN ratings ON\n" +
                    "ratings.movieId = m.id) as mr\n" +
                    "LEFT JOIN \n" +
                    "(SELECT stars_in_movies.movieId, group_concat(stars.id order by stars.id) as starId, group_concat(stars.name order by stars.id) as starName\n" +
                    "FROM stars, stars_in_movies\n" +
                    "WHERE stars.id = stars_in_movies.starId\n" +
                    "group by stars_in_movies.movieId\n" +
                    ") as star \n" +
                    "ON star.movieId = mr.id\n" +
                    ";";

            // Single connection
            // PreparedStatement statement = con.prepareStatement(query);

            // Connection pooling
            PreparedStatement statement = dbcon.prepareStatement(query);

            statement.setString(1, t);

            ResultSet rs = statement.executeQuery();
            JsonArray jsonArray = new JsonArray();

            while(rs.next()){

                String id = rs.getString("mr.id");
                String title = rs.getString("mr.title");
                String year = rs.getString("mr.year");
                String director = rs.getString("mr.director");
                String genres = rs.getString("mr.genre");
                String stars = rs.getString("star.starName");
                String starId = rs.getString("star.starId");
                String rating = rs.getString("mr.rating");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", id);
                jsonObject.addProperty("movie_title", title);
                jsonObject.addProperty("movie_year", year);
                jsonObject.addProperty("movie_director", director);
                jsonObject.addProperty("movie_genres", genres);
                jsonObject.addProperty("movie_stars", stars);
                jsonObject.addProperty("star_id",starId);
                jsonObject.addProperty("movie_rating", rating);

                jsonArray.add(jsonObject);
            }

            out.write(jsonArray.toString());
            System.out.println(jsonArray.toString());
            response.setStatus(200);
            rs.close();
            statement.close();
            // Single connection
            // con.close();

            // Connection pooling
            dbcon.close();

        }catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();
    }
}