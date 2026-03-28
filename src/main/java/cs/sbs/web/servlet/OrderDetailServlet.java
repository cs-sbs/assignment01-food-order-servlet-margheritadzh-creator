package cs.sbs.web.servlet;

import cs.sbs.web.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
            resp.getWriter().print("Error: order id is required");
            return;
        }

        String idStr = pathInfo.substring(1); // 去掉前面的 "/"

        int orderId;
        try {
            orderId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.getWriter().print("Error: order id must be a valid number");
            return;
        }

        List<Order> orderList = getOrInitOrderList();
        Order target = null;

        for (Order order : orderList) {
            if (order.getId() == orderId) {
                target = order;
                break;
            }
        }

        if (target == null) {
            resp.getWriter().print("Error: order not found");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Order Detail\n\n");
        sb.append("Order ID: ").append(target.getId()).append("\n");
        sb.append("Customer: ").append(target.getCustomer()).append("\n");
        sb.append("Food: ").append(target.getFood()).append("\n");
        sb.append("Quantity: ").append(target.getQuantity());

        resp.getWriter().print(sb.toString());
    }

    @SuppressWarnings("unchecked")
    private List<Order> getOrInitOrderList() {
        ServletContext context = getServletContext();
        List<Order> orderList = (List<Order>) context.getAttribute("orderList");

        if (orderList == null) {
            synchronized (context) {
                orderList = (List<Order>) context.getAttribute("orderList");
                if (orderList == null) {
                    orderList = new ArrayList<>();
                    context.setAttribute("orderList", orderList);
                }
            }
        }
        return orderList;
    }
}