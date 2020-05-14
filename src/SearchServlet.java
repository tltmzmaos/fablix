import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet(name = "SearchServlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String searchInput = request.getParameter("searchBar").toLowerCase();

        PrintWriter out = response.getWriter();
        try {
            Connection con = dataSource.getConnection();

            String ft_search = "SELECT id FROM movies WHERE MATCH (title) AGAINST (? IN BOOLEAN MODE);";
            PreparedStatement ft_st = con.prepareStatement(ft_search);
            ft_st.setString(1, searchInput);
            ResultSet ft_rs = ft_st.executeQuery();

            JsonArray jsonArray = new JsonArray();
            while (ft_rs.next()) {

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
                        "movies.id = ?\n" +
                        "group by movies.id) as gs\n" +
                        "LEFT JOIN ratings ON\n" +
                        "ratings.movieId = gs.id\n" +
                        ";";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, ft_rs.getString("id"));
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("gs.id");
                    String title = rs.getString("gs.title");
                    String year = rs.getString("gs.year");
                    String director = rs.getString("gs.director");
                    String genres = rs.getString("gs.genres");
                    String stars = rs.getString("gs.stars");
                    String starId = rs.getString("gs.starId");
                    String rating = rs.getString("rating");

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("movie_id", id);
                    jsonObject.addProperty("movie_title", title);
                    jsonObject.addProperty("movie_year", year);
                    jsonObject.addProperty("movie_director", director);
                    jsonObject.addProperty("movie_genres", genres);
                    jsonObject.addProperty("movie_stars", stars);
                    jsonObject.addProperty("star_id", starId);
                    jsonObject.addProperty("movie_rating", rating);

                    jsonArray.add(jsonObject);
                }
                rs.close();
                statement.close();
            }

//            String query = "select gs.id, gs.title, gs.year, gs.director, gs.genres, gs.genreId, gs.stars, gs.starId, rating\n" +
//                    "from\n" +
//                    "(select movies.id, title, director, year, group_concat(distinct genres.name) as genres,\n" +
//                    "group_concat(distinct genres.id order by genres.name) as genreId,\n" +
//                    "group_concat(distinct stars.name) as stars,\n" +
//                    "group_concat(distinct stars.id order by stars.name) as starId\n" +
//                    "from movies, stars, stars_in_movies, genres, genres_in_movies\n" +
//                    "where\n" +
//                    "movies.id = stars_in_movies.movieId and\n" +
//                    "stars.id = stars_in_movies.starId and\n" +
//                    "genres_in_movies.movieId = movies.id and\n" +
//                    "genres.id = genres_in_movies.genreId and\n" +
//                    "movies.title like ?\n"+
//                    "group by movies.id) as gs\n" +
//                    "LEFT JOIN ratings ON\n" +
//                    "ratings.movieId = gs.id\n" +
//                    ";";
//
//            PreparedStatement statement = con.prepareStatement(query);
//            statement.setString(1, "%" + searchInput + "%");
//            ResultSet rs = statement.executeQuery();
//
//            JsonArray jsonArray = new JsonArray();
//            while (rs.next()){
//                String id = rs.getString("gs.id");
//                String title = rs.getString("gs.title");
//                String year = rs.getString("gs.year");
//                String director = rs.getString("gs.director");
//                String genres = rs.getString("gs.genres");
//                String stars = rs.getString("gs.stars");
//                String starId = rs.getString("gs.starId");
//                String rating = rs.getString("rating");
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("movie_id", id);
//                jsonObject.addProperty("movie_title", title);
//                jsonObject.addProperty("movie_year", year);
//                jsonObject.addProperty("movie_director", director);
//                jsonObject.addProperty("movie_genres", genres);
//                jsonObject.addProperty("movie_stars", stars);
//                jsonObject.addProperty("star_id",starId);
//                jsonObject.addProperty("movie_rating", rating);
//
//                jsonArray.add(jsonObject);
//            }
            out.write(jsonArray.toString());
            response.setStatus(200);
//            rs.close();
//            statement.close();
            ft_rs.close();
            ft_st.close();
            con.close();


        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            response.getWriter().write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
