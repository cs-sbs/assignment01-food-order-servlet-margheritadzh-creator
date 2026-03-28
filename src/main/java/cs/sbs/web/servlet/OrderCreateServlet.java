package cs.sbs.web.servlet;

import cs.sbs.web.model.MenuItem;
import cs.sbs.web.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String customer = req.getParameter("customer");
        String food = req.getParameter("food");
        String quantityStr = req.getParameter("quantity");

        if (isBlank(customer) || isBlank(food) || isBlank(quantityStr)) {
            resp.getWriter().print("Error: required parameters cannot be empty");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr.trim());
            if (quantity <= 0) {
                resp.getWriter().print("Error: quantity must be a valid number");
                return;
            }
        } catch (NumberFormatException e) {
            resp.getWriter().print("Error: quantity must be a valid number");
            return;
        }

        List<MenuItem> menuList = getOrInitMenuList();
        String matchedFoodName = findFoodName(menuList, food.trim());
        if (matchedFoodName == null) {
            resp.getWriter().print("Error: food not found in menu");
            return;
        }

        List<Order> orderList = getOrInitOrderList();
        AtomicInteger nextOrderId = getOrInitNextOrderId();

        int newId = nextOrderId.getAndIncrement();
        Order order = new Order(newId, customer.trim(), matchedFoodName, quantity);

        synchronized (getServletContext()) {
            orderList.add(order);
        }

        String detailUrl = req.getContextPath() + "/order/" + newId;

        StringBuilder sb = new StringBuilder();
        sb.append("Order Created: ").append(newId).append("\n");
        sb.append("Customer: ").append(order.getCustomer()).append("\n");
        sb.append("Food: ").append(order.getFood()).append("\n");
        sb.append("Quantity: ").append(order.getQuantity()).append("\n");
        sb.append("View Detail: ").append(detailUrl);

        resp.getWriter().print(sb.toString());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String findFoodName(List<MenuItem> menuList, String inputFood) {
        String keyword = inputFood.trim().toLowerCase();

        for (MenuItem item : menuList) {
            if (item.getName().toLowerCase().contains(keyword)) {
                return item.getName();
            }
        }
        return null;
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

    private AtomicInteger getOrInitNextOrderId() {
        ServletContext context = getServletContext();
        AtomicInteger nextOrderId = (AtomicInteger) context.getAttribute("nextOrderId");

        if (nextOrderId == null) {
            synchronized (context) {
                nextOrderId = (AtomicInteger) context.getAttribute("nextOrderId");
                if (nextOrderId == null) {
                    nextOrderId = new AtomicInteger(1001);
                    context.setAttribute("nextOrderId", nextOrderId);
                }
            }
        }
        return nextOrderId;
    }
}