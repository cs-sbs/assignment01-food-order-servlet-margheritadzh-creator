package cs.sbs.web.servlet;

import cs.sbs.web.model.MenuItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().println("TODO: implement menu List");

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        List<MenuItem> menuList = getOrInitMenuList();

        String keyword = req.getParameter("name");
        StringBuilder sb = new StringBuilder();
        sb.append("Menu List:\n\n");

        int index = 1;
        for (MenuItem item : menuList) {
            if (keyword == null || keyword.trim().isEmpty()
                    || item.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                sb.append(index++)
                        .append(". ")
                        .append(item.getName())
                        .append(" - $")
                        .append(item.getPrice())
                        .append("\n");
            }
        }

        if (index == 1) {
            sb.append("No menu items found.\n");
        }

        resp.getWriter().print(sb.toString());
    }

    @SuppressWarnings("unchecked")
    private List<MenuItem> getOrInitMenuList() {
        ServletContext context = getServletContext();
        List<MenuItem> menuList = (List<MenuItem>) context.getAttribute("menuList");

        if (menuList == null) {
            synchronized (context) {
                menuList = (List<MenuItem>) context.getAttribute("menuList");
                if (menuList == null) {
                    menuList = new ArrayList<>();
                    menuList.add(new MenuItem("Fried Rice", 8));
                    menuList.add(new MenuItem("Fried Noodles", 9));
                    menuList.add(new MenuItem("Burger", 10));
                    context.setAttribute("menuList", menuList);
                }
            }
        }
        return menuList;
    }
}