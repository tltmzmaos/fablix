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
import java.sql.*;


@WebServlet(name = "DashboardServlet", urlPatterns = "/metadata")
public class DashboardServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            Connection con = dataSource.getConnection();
            JsonObject total = new JsonObject();

            DatabaseMetaData dbms = con.getMetaData();
            ResultSet temp = dbms.getTables(null, null, null, new String[]{"TABLE"});
            while(temp.next()){
                JsonArray jsonArray = new JsonArray();
                String temp_query = "SELECT * FROM " + temp.getString("Table_NAME") + ";";
                PreparedStatement temp_s = con.prepareStatement(temp_query);
                ResultSet temp_rs = temp_s.executeQuery();
                ResultSetMetaData temp_md = temp_rs.getMetaData();

                int count = temp_md.getColumnCount();
                for(int i=1; i<=count; i++){
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("column_name", temp_md.getColumnName(i));
                    jsonObject.addProperty("column_type", temp_md.getColumnTypeName(i));
                    jsonObject.addProperty("nullable", temp_md.isNullable(i));
                    jsonObject.addProperty("increment", temp_md.isAutoIncrement(i));
                    jsonArray.add(jsonObject);
                }
                total.add(temp.getString("Table_NAME"), jsonArray);
                temp_s.close();
                temp_rs.close();
            }

            out.write(total.toString());
            response.setStatus(200);

            con.close();

        }catch (Exception e){
            System.out.println(e.toString());
        }
        out.close();
    }
}
