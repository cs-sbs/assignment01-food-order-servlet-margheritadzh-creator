package cs.sbs.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private int id;
    private String customer;
    private String food;
    private int quantity;

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1001);
    private static final List<Order> ORDERS = new ArrayList<>();

    public Order(int id, String customer, String food, int quantity) {
        this.id = id;
        this.customer = customer;
        this.food = food;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getFood() {
        return food;
    }

    public int getQuantity() {
        return quantity;
    }

    public static synchronized Order create(String customer, String food, int quantity) {
        Order order = new Order(ID_GENERATOR.getAndIncrement(), customer, food, quantity);
        ORDERS.add(order);
        return order;
    }

    public static synchronized Order findById(int id) {
        for (Order order : ORDERS) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }
}