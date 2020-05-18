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
import java.util.HashMap;

@WebServlet("/movie-suggestion")
public class MovieSuggestionServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    HashMap<String, String> titleMap = new HashMap<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchInput = request.getParameter("query").toLowerCase();
        PrintWriter out = response.getWriter();
        try{
            Connection con = dataSource.getConnection();
            JsonArray jsonArray = new JsonArray();
            titleMap = getMap(con);
            if(titleMap.isEmpty() || searchInput.length() < 3){
                out.write(jsonArray.toString());
                return;
            }
            int count = 0;
            for(String i : titleMap.keySet()){
                String title = titleMap.get(i);
                if(title.toLowerCase().contains(searchInput)){
                    if(count == 10){
                        break;
                    }
                    jsonArray.add(generateJsonObject(i, title));
                    count++;
                }
            }
            out.write(jsonArray.toString());
            response.setStatus(200);
            con.close();
            return;
        }catch (Exception e){
            System.out.println(e.toString());
            response.sendError(500, e.getMessage());
        }
    }

    private static HashMap<String, String> getMap(Connection con){
        HashMap<String, String> temp = new HashMap<String, String>();
        try{
            String ft_search = "SELECT id, title FROM movies;";
            PreparedStatement ft_st = con.prepareStatement(ft_search);
            ResultSet ft_rs = ft_st.executeQuery();

            while(ft_rs.next()){
                String new_title = ft_rs.getString("title");
                String new_id = ft_rs.getString("id");
                temp.put(new_id, new_title);
            }
            ft_rs.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return temp;
    }

    private static JsonObject generateJsonObject(String id, String title) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", title);
        JsonObject additionalDataJsonObject = new JsonObject();
        additionalDataJsonObject.addProperty("id", id);
        jsonObject.add("data", additionalDataJsonObject);
        return jsonObject;
    }
}
