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
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/movie-suggestion")
public class MovieSuggestionServlet extends HttpServlet {
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    //HashMap<String, String> titleMap = new HashMap<>();
    ArrayList<HashMap<String, String>> titleList = new ArrayList<>();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchInput = request.getParameter("query").toLowerCase();
        PrintWriter out = response.getWriter();
        try{
            // Single connection
            // Connection con = dataSource.getConnection();

            // Connection pooling
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
            if (ds == null)
                out.println("ds is null.");
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
            dbcon.setReadOnly(true);

            JsonArray jsonArray = new JsonArray();
            // Single connection
            // titleList = getMap(con, searchInput);

            // Connection pooling
            titleList = getMap(dbcon, searchInput);

            if(titleList.isEmpty() || searchInput.length() < 3){
                out.write(jsonArray.toString());
                return;
            }else{
                for(int i=0; i<titleList.size(); i++){
                    for(String j : titleList.get(i).keySet()){
                        String title = titleList.get(i).get(j);
                        jsonArray.add(generateJsonObject(j, title));
                    }
                }
            }
            out.write(jsonArray.toString());
            response.setStatus(200);
            // Single connection
            // con.close();

            // Connection pooling
            dbcon.close();

            return;
        }catch (Exception e){
            System.out.println(e.toString());
            response.sendError(500, e.getMessage());
        }
    }
    private static ArrayList<HashMap<String, String>> getMap(Connection con, String searchInput){
        ArrayList<HashMap<String, String>> rturn = new ArrayList<>();
        try{

            String[] input_split = searchInput.split(" ");
            String prefix_query = "SELECT id, title FROM movies WHERE MATCH (title) AGAINST (";
            StringBuffer pr = new StringBuffer(prefix_query);
            for(int i=0; i<input_split.length; i++){
                pr.append("'+"+ input_split[i] +"*'");
            }
            pr.append("IN BOOLEAN MODE) LIMIT 10;");
            PreparedStatement p_s = con.prepareStatement(String.valueOf(pr));
            ResultSet ft_rs = p_s.executeQuery();

            while(ft_rs.next()){
                String query = "select id, title from movies where id=?;";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, ft_rs.getString("id"));
                ResultSet rs = statement.executeQuery();

                while(rs.next()){
                    HashMap<String, String> temp = new HashMap<String, String>();
                    String new_title = rs.getString("title");
                    String new_id = rs.getString("id");
                    temp.put(new_id, new_title);
                    rturn.add(temp);
                }

            }
            ft_rs.close();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return rturn;
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
