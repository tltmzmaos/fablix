import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/employeeLogin")
public class EmployeeLoginServlet extends HttpServlet {
    // Single connection
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("employeename");
        String password = request.getParameter("employeepassword");

        String databaseUserEamil = "";
        String databaseUserPassword = "";

        PrintWriter out = response.getWriter();

        System.out.println("id: "+username + "\npassword: "+password);

        try {
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

            String query = "select * from employees where email=\"" + username +"\";";
            // Single connection
            // PreparedStatement statement = con.prepareStatement(query);

            // Connection pooling
            PreparedStatement statement = dbcon.prepareStatement(query);

            ResultSet rs = statement.executeQuery();
            JsonObject responseJsonObject = new JsonObject();
            while(rs.next()) {
                databaseUserEamil = rs.getString("email");
                databaseUserPassword = rs.getString("password");
            }

            System.out.println(verifyCredentials(username, password));

            if(databaseUserEamil==""){
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            }
            //else if(!password.equals(databaseUserPassword)){
            else if(!verifyCredentials(username, password)){
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "incorrect password");
            }else{
                request.getSession().setAttribute("user", new User(username));
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            }
            out.write(responseJsonObject.toString());

            rs.close();
            statement.close();
            // Single connection
            // con.close();

            // Connection pooling
            dbcon.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }
        out.close();
    }

    private static boolean verifyCredentials(String email, String password) throws Exception {

        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        //Statement statement = connection.createStatement();

        String query = String.format("SELECT * from employees where email='%s'", email);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        //ResultSet rs = statement.executeQuery(query);

        boolean success = false;
        if (rs.next()) {
            // get the encrypted password from the database
            String encryptedPassword = rs.getString("password");

            // use the same encryptor to compare the user input password with encrypted password stored in DB
            success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
        }

        rs.close();
        statement.close();
        connection.close();

        System.out.println("verify " + email + " - " + password);

        return success;
    }

}
