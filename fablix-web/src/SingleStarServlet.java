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

@WebServlet(name = "SingleStarServlet", urlPatterns = "/single-star")
public class SingleStarServlet extends HttpServlet {

    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String s = request.getParameter("id");
        try{
            Connection con = dataSource.getConnection();
            String query = "select stars.id, stars.name, stars.birthYear ,GROUP_CONCAT(distinct movies.title) as title, group_concat(distinct movies.id) as movieId\n" +
                    "from stars, stars_in_movies, movies\n" +
                    "where stars.id = stars_in_movies.starId and\n" +
                    "stars_in_movies.movieId = movies.id and stars.id = ?\n" +
                    "group by stars.id;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while(rs.next()){
                String starId = rs.getString("stars.id");
                String starName = rs.getString("stars.name");
                String starBY = rs.getString("stars.birthYear");
                String movies = rs.getString("title");
                String movieId = rs.getString("movieId");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("star_id", starId);
                jsonObject.addProperty("star_name", starName);
                jsonObject.addProperty("star_by", starBY);
                jsonObject.addProperty("movies", movies);
                jsonObject.addProperty("movieId", movieId);

                jsonArray.add(jsonObject);
            }
            out.write(jsonArray.toString());
            response.setStatus(200);
            rs.close();
            statement.close();
            con.close();
        } catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
