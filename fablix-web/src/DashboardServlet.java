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
import java.sql.*;


@WebServlet(name = "DashboardServlet", urlPatterns = "/metadata")
public class DashboardServlet extends HttpServlet {
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            // Single connection
            // Connection con = dataSource.getConnection();

            // Connection pooling
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            //DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/rw");
            if (ds == null)
                out.println("ds is null.");
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
            dbcon.setReadOnly(true);

            JsonObject total = new JsonObject();

            // Single connection
            // DatabaseMetaData dbms = con.getMetaData();

            // Connection pooling
            DatabaseMetaData dbms = dbcon.getMetaData();

            ResultSet temp = dbms.getTables(null, null, null, new String[]{"TABLE"});
            while(temp.next()){
                JsonArray jsonArray = new JsonArray();
                String temp_query = "SELECT * FROM " + temp.getString("Table_NAME") + ";";

                // Single connection
               //PreparedStatement temp_s = con.prepareStatement(temp_query);

                // Connection pooling
                PreparedStatement temp_s = dbcon.prepareStatement(temp_query);

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

            // Single connection
            // con.close();

            // Connection pooling
            dbcon.close();

        }catch (Exception e){
            System.out.println(e.toString());
        }
        out.close();
    }
}
