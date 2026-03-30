package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantityText = req.getParameter("quantity");

        if (isBlank(customer) || isBlank(food) || isBlank(quantityText)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Error: missing required parameter");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText.trim());
            if (quantity <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("Error: invalid quantity");
                return;
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Error: invalid quantity");
            return;
        }

        Order order = Order.create(customer.trim(), food.trim(), quantity);

        resp.getWriter().print(
                "Order Created\n" +
                        "Order ID: " + order.getId() + "\n" +
                        "Customer: " + order.getCustomer() + "\n" +
                        "Food: " + order.getFood() + "\n" +
                        "Quantity: " + order.getQuantity()
        );
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}