package org.example.ui.menus.admin.orders;

import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Menu;
import org.example.ui.menus.admin.orders.list.OrdersHandler;

import java.util.*;

public class ListOrders extends Menu {

    OrderService orderService = new OrderService();

    List<Order> orders;

    public void run() {
        super.run();

        while (running) menu();
    }

    private void menu() {
        orders = orderService.getAllOrders();

        p.menu("LIST ORDERS");
        System.out.println(
                "[1] All (" + orders.size() + ") \n" +
                "[2] Pending (" + ordersByStatus(1).size() + ") \n" +
                "[3] In Progress (" + ordersByStatus(2).size() + ") \n" +
                "[4] Closed (" + ordersByStatus(3).size() + ") \n" +
                "[0] Exit\n"
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "0", "exit" -> exit();
            case "1", "all" -> new OrdersHandler(orders, "All");
            case "2", "pending" -> new OrdersHandler(ordersByStatus(1), "Pending");
            case "3", "active" -> new OrdersHandler(ordersByStatus(2), "Active");
            case "4", "closed" -> printClosedOrders(ordersByStatus(3));
            default -> p.invalidCommand();
        }
    }

    private List<Order> ordersByStatus(Integer statusId) {
        return orders.stream().filter(o -> Objects.equals(o.getOrderStatusId(), statusId)).toList();
    }

    private void printClosedOrders(List<Order> closedOrders) {
        p.printH1("CLOSED ORDERS HISTORY");
        p.printEntities(closedOrders);
    }

}
