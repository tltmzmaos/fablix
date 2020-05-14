import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
@WebServlet(name = "ChangeQuantity", urlPatterns = "/api/change-quantity")
public class ChangeQuantity extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String movietitle = request.getParameter("movie-title");
        String quantity = request.getParameter("quantity");

        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

        // prevent corrupted states through sharing under multi-threads
        // will only be executed by one thread at a time
        synchronized (previousItems) {

            for (int i = 0; i < previousItems.size(); i ++) {
                if (previousItems.get(i).equals(movietitle)) {

                    int new_quantity = Integer.parseInt(quantity);
                    previousItems.set(i+1,Integer.toString(new_quantity));

                }
            }


        }

        response.getWriter().write(String.join(",", previousItems));
    }
}


