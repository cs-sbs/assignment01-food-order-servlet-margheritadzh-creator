package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String uri = req.getRequestURI();   // e.g. /order/1001
        String contextPath = req.getContextPath(); // usually ""
        String path = uri.substring(contextPath.length());

        String prefix = "/order/";
        if (!path.startsWith(prefix) || path.length() <= prefix.length()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Error: invalid order id");
            return;
        }

        String idText = path.substring(prefix.length()).trim();

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Error: invalid order id");
            return;
        }

        Order order = Order.findById(id);
        if (order == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print("Error: order not found");
            return;
        }

        resp.getWriter().print(
                "Order Detail\n" +
                        "Order ID: " + order.getId() + "\n" +
                        "Customer: " + order.getCustomer() + "\n" +
                        "Food: " + order.getFood() + "\n" +
                        "Quantity: " + order.getQuantity()
        );
    }
}