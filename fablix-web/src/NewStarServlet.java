import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet(name = "NewStarServlet", urlPatterns = "/new-star")
public class NewStarServlet extends HttpServlet {
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String star_name = request.getParameter("new_star_name");
        String star_year = request.getParameter("new_star_year");

        PrintWriter out = response.getWriter();
        try{
            // Single connection
            // Connection con = dataSource.getConnection();

            // Connection pooling
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedbreadwrite");
            if (ds == null)
                out.println("ds is null.");
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");

            String getNewId = "Select max(id) as maxId from stars;";
            // Single connection
            // PreparedStatement idStatement = con.prepareStatement(getNewId);

            // Connection pooling
            PreparedStatement idStatement = dbcon.prepareStatement(getNewId);

            ResultSet rs = idStatement.executeQuery();
            String newId = "";

            while(rs.next()){
                String temp = rs.getString("maxId");
                newId = "nm" + (Integer.parseInt(temp.substring(2)) + 1);
            }

            String query = "INSERT INTO stars VALUES(?,?,?);";
            // Single connection
            // PreparedStatement statement = con.prepareStatement(query);

            // Connection pooling
            PreparedStatement statement = dbcon.prepareStatement(query);

            JsonObject jsonObject = new JsonObject();

            statement.setString(1, newId);
            statement.setString(2, star_name);
            if(star_year.equals("")){
                statement.setNull(3, Types.NULL);
            }else{
                statement.setString(3, star_year);
            }
            statement.executeUpdate();

            System.out.println("NewStarServlet\nstar id: " + newId +
                    "\nstar name: " + star_name + "\nstar year: "+star_year);

            jsonObject.addProperty("star_id", newId);
            jsonObject.addProperty("star_name", star_name);
            jsonObject.addProperty("star_year", star_year);
            jsonObject.addProperty("message", "New star, " + star_name +"(star id: " + newId + "), is successfully added");

            out.write(jsonObject.toString());

            rs.close();
            idStatement.close();
            statement.close();
            // Single connection
            // con.close();

            // Connection pooling
            dbcon.close();

        }catch (Exception e){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "fail");
            jsonObject.addProperty("message", "New star is not added./\n"+e.toString());
            System.out.println(e.toString());
        }
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
