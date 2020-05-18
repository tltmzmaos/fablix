import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "CheckoutServlet", urlPatterns = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        String cc_number = request.getParameter("cc-number");
        String expiration = request.getParameter("cc-expiration");

        String databaseCCID = "";
        String databaseUserPassword = "";

        System.out.println("id: "+ cc_number + "\ncc-expiration: "+expiration);

        try {
            Connection con = dataSource.getConnection();
            String query = "SELECT * from creditcards, customers\n" +
                    "where creditcards.id = ?  and creditcards.id = customers.id and creditcards.expiration = ?";



            PreparedStatement statement = con.prepareStatement(query);

            statement.setString(1, cc_number);
            statement.setString(2, expiration);

            ResultSet rs = statement.executeQuery();

            JsonObject responseJsonObject = new JsonObject();

            while(rs.next()) {
                databaseCCID = rs.getString("id");

            }

            if(databaseCCID == ""){
                System.out.println("fail");
                responseJsonObject.addProperty("status", "fail");

            } else{
                System.out.println("success");
                responseJsonObject.addProperty("status", "success");

                for (int i = 0; i < previousItems.size(); i+=2) {
                    String movie = previousItems.get(i);
                    System.out.println("movie: " + movie);
                    String movie_id = "";

                    String movie_id_query = "select id from movies where title = ? limit 1;";
                    PreparedStatement movie_id_statement = con.prepareStatement(movie_id_query);
                    movie_id_statement.setString(1, movie);
                    ResultSet movie_result_set = movie_id_statement.executeQuery();

                    while (movie_result_set.next()) {
                        movie_id = movie_result_set.getString("id");
                        System.out.println(movie_id);
                    }


                    String sale_query = "INSERT INTO sales VALUES(?,?,?,?);";
                    PreparedStatement insert_statement = con.prepareStatement(sale_query);
                    insert_statement.setNull(1, java.sql.Types.NULL);
                    insert_statement.setString(2, cc_number);
                    insert_statement.setString(3, movie_id);
                    insert_statement.setDate(4, Date.valueOf(java.time.LocalDate.now()));
                    insert_statement.executeUpdate();


                }
                //get sale id
                String sale_id  = "";
                String sale_id_query = "SELECT id from sales ORDER BY id desc limit 1;";
                PreparedStatement sale_id_statement = con.prepareStatement(sale_id_query);
                ResultSet sale_id_result_set = sale_id_statement.executeQuery();

                while (sale_id_result_set.next()) {
                    sale_id = sale_id_result_set.getString("id");
                    System.out.println("sale_id " + sale_id);
                    responseJsonObject.addProperty("sale_id", sale_id);
                }


            }

            response.getWriter().write(responseJsonObject.toString());

            rs.close();
            statement.close();
            con.close();

        }catch(Exception e){
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");

            System.out.println(e.toString());
        }
    }
}
