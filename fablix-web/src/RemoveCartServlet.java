import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "RemoveCartServlet", urlPatterns = "/api/remove")
public class RemoveCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String movietitle = request.getParameter("movie-title");

        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        ArrayList<String> previousItems = (ArrayList<String>) session.getAttribute("previousItems");

            // prevent corrupted states through sharing under multi-threads
            // will only be executed by one thread at a time
            synchronized (previousItems) {

                for (int i = 0; i < previousItems.size(); i ++) {
                    if (previousItems.get(i).equals(movietitle)) {
                        previousItems.remove(i+1);
                        previousItems.remove(i);
                    }
                }


            }

        response.getWriter().write(String.join(",", previousItems));
    }
}


