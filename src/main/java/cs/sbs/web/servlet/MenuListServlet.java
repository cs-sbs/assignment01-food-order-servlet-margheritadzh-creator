package cs.sbs.web.servlet;

import cs.sbs.web.model.MenuItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuListServlet extends HttpServlet {

    private static final List<MenuItem> MENU = new ArrayList<>();

    static {
        MENU.add(new MenuItem("Fried Rice", 8));
        MENU.add(new MenuItem("Fried Noodles", 9));
        MENU.add(new MenuItem("Burger", 10));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String keyword = req.getParameter("name");
        StringBuilder sb = new StringBuilder();
        sb.append("Menu List:\n");

        boolean found = false;

        for (MenuItem item : MENU) {
            if (keyword == null || keyword.trim().isEmpty()
                    || item.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                sb.append(item.getName())
                        .append(" - $")
                        .append(item.getPrice())
                        .append("\n");
                found = true;
            }
        }

        if (!found) {
            sb.append("No menu found");
        }

        resp.getWriter().print(sb.toString());
    }
}