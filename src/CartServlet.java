
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        HttpSession session = request.getSession();


        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < previousItems.size(); i+=2) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("movie_title", previousItems.get(i));
            jsonObject.addProperty("quantity", previousItems.get(i+1));
            jsonArray.add(jsonObject);
        }


        // write all the data into the jsonObject
        response.getWriter().write(jsonArray.toString());
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String movietitle = request.getParameter("movie-title");
        String quantity = request.getParameter("quantity");
        System.out.println(movietitle);
        System.out.println(quantity);
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new ArrayList<>();
            previousItems.add(movietitle);
            previousItems.add(quantity);
            session.setAttribute("previousItems", previousItems);
        } else {
            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time

            boolean movie_already_in_cart = false;
            System.out.println("more than 1 movie");
            synchronized (previousItems) {

                for (int i = 0; i < previousItems.size(); i ++) {
                    if (previousItems.get(i).equals(movietitle)) {
                        int new_quantity = Integer.parseInt(previousItems.get(i+1)) + 1;
                        previousItems.set(i+1,Integer.toString(new_quantity));
                        movie_already_in_cart = true;

                    }
                }
                if (movie_already_in_cart == false) {
                    previousItems.add(movietitle);
                    previousItems.add(quantity);
                }

            }
        }

        response.getWriter().write(String.join(",", previousItems));
    }
}
