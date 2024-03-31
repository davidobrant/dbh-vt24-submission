package org.example.ui.menus.admin.orders.list;

import org.example.models.Order;
import org.example.services.EmployeeService;
import org.example.supers.Menu;
import org.example.ui.menus.admin.orders.list.handler.HandleInProgress;
import org.example.ui.menus.admin.orders.list.handler.HandlePending;

import java.util.List;

public class OrdersHandler extends Menu {

    List<Order> orders;
    String menuTitle;

    public OrdersHandler(List<Order> orders, String title) {
        this.orders = orders;
        this.menuTitle = title;
        super.run();

        while (running) {
            p.menu(menuTitle.toUpperCase() + " ORDERS");
            var index = u.printChoosingList(orders);
            if (index == null) return;
            var order = orders.get(index);
            if (order == null) return;
            handleOrder(order);
        }
    }

    private void handleOrder(Order order) {
        p.printEntity(order);
        switch (order.getOrderStatusId().toString()) {
            case "1" -> new HandlePending(order);
            case "2" -> new HandleInProgress(order);
            default -> p.invalidCommand();
        }
    }

}
