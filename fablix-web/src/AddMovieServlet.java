import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AddMovieServlet", urlPatterns = "/add-movie")
public class AddMovieServlet extends HttpServlet {

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String movietitle = request.getParameter("movie-title");
        String movieyear = request.getParameter("movie-year");

        String movieDirector = request.getParameter("movie-director");
        String starName = request.getParameter("star-name");
        String genreName = request.getParameter("genre-name");

        System.out.println("Movie title " + movietitle);

        try {
            Connection con = dataSource.getConnection();
            String newMovieId = "";
            String newMovieIdQuery = "SELECT max(id) as maxId from movies where id like 'tt0%';";


            PreparedStatement movieIdStatement = con.prepareStatement(newMovieIdQuery);
            ResultSet movieIdResultSet = movieIdStatement.executeQuery();

            while (movieIdResultSet.next()){
                String maxid = movieIdResultSet.getString("maxId");
                newMovieId = "tt0" + (Integer.parseInt(maxid.substring(2)) + 1);
            }

            String newStarId = "";
            String newStarQuery = "SELECT max(id) as maxId from stars;";

            PreparedStatement starIdStatement = con.prepareStatement(newStarQuery);
            ResultSet starIdResultSet = starIdStatement.executeQuery();

            while (starIdResultSet.next()) {
                String maxStarId = starIdResultSet.getString("maxId");
                newStarId = "nm" + (Integer.parseInt(maxStarId.substring(2)) +1);
            }


            String addMovieQuery = "CALL add_movie( ?, ?, ?, ?, ?, ?, ?);";
            System.out.println("Called stored procedure");
            PreparedStatement statement = con.prepareStatement(addMovieQuery);

            statement.setString(1, newMovieId);
            statement.setString(2, movietitle);
            statement.setInt(3, Integer.parseInt(movieyear));
            statement.setString(4, movieDirector);
            statement.setString(5, newStarId);
            statement.setString(6, starName);
            statement.setString(7, genreName);

            ResultSet rs = statement.executeQuery();

            JsonObject jsonObject = new JsonObject();
            System.out.println("Get max movie id ");
            String newMovieId2 = "";
            String newMovieIdQuery2 = "SELECT max(id) as maxId from movies where id like 'tt0%';";


            PreparedStatement movieIdStatement2 = con.prepareStatement(newMovieIdQuery2);
            ResultSet movieIdResultSet2 = movieIdStatement2.executeQuery();

            while (movieIdResultSet2.next()){
                newMovieId2 = movieIdResultSet2.getString("maxId");

            }

            System.out.println("Get Genre Id ");
            //get genre id
            String genreId = "";
            String genreQuery = "SELECT id from genres where name = ?";
            PreparedStatement genreStatement = con.prepareStatement(genreQuery);
            genreStatement.setString(1, genreName);
            ResultSet genreResultSet = genreStatement.executeQuery();

            while (genreResultSet.next()) {
                genreId = genreResultSet.getString("id");
            }

            System.out.println("Get star Id ");
            String starId = "";
            String starQuery = "SELECT id from stars where name = ?";
            PreparedStatement starStatement = con.prepareStatement(starQuery);
            starStatement.setString(1, starName);
            ResultSet starResultSet = starStatement.executeQuery();

            while (starResultSet.next()) {
                starId = starResultSet.getString("id");
            }
            System.out.println("starid " + starId);
            System.out.println("check movie ");
            if (newMovieId2.equals(newMovieId)) {
                //movie added
                jsonObject.addProperty("message", "New movie, " + movietitle +" (id: " + newMovieId + "), " +
                        "star id = " + starId + ", genre id = " + genreId + " successfully added");
            } else {
                //movie not added
                jsonObject.addProperty("message", "Movie: " + movietitle + " exists");
            }

            response.getWriter().write(jsonObject.toString());

            rs.close();
            statement.close();
            con.close();

        }catch(Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("message", "New movie was not added./\n"+e.toString());

            System.out.println(e.toString());
        }
    }
}