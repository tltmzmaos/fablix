import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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


@WebServlet(name = "AdvancedSearchServlet", urlPatterns = "/advanced-search")
public class AdvancedSearchServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String titleInput = request.getParameter("title").toLowerCase();
        String yearInput = request.getParameter("year").toLowerCase();
        String directorInput = request.getParameter("director").toLowerCase();
        String starInput = request.getParameter("star").toLowerCase();

        PrintWriter out = response.getWriter();

        try{
            Connection con = dataSource.getConnection();

            String query = "select gs.id, gs.title, gs.year, gs.director, gs.genres, gs.genreId, gs.stars, gs.starId, rating\n" +
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
                    "movies.title like ?and\n"+
                    "movies.year like ?and\n" +
                    "movies.director like ?and\n" +
                    "stars.name like ?\n" +
                    "group by movies.id) as gs\n" +
                    "LEFT JOIN ratings ON\n" +
                    "ratings.movieId = gs.id\n" +
                    ";";

            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, "%" + titleInput + "%");
            statement.setString(2, "%" + yearInput + "%");
            statement.setString(3, "%" + directorInput + "%");
            statement.setString(4, "%" + starInput + "%");
            ResultSet resultSets = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            while (resultSets.next()){
                String id = resultSets.getString("gs.id");
                String title = resultSets.getString("gs.title");
                String year = resultSets.getString("gs.year");
                String director = resultSets.getString("gs.director");
                String genres = resultSets.getString("gs.genres");
                String stars = resultSets.getString("gs.stars");
                String starId = resultSets.getString("gs.starId");
                String rating = resultSets.getString("rating");

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
            response.setStatus(200);
            resultSets.close();
            statement.close();
            con.close();

        }catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            response.getWriter().write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
