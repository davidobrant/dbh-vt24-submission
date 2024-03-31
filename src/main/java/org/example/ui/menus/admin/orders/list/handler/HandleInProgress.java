package org.example.ui.menus.admin.orders.list.handler;

import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Menu;

public class HandleInProgress extends Menu {

    OrderService orderService = new OrderService();
    Order order;

    public HandleInProgress(Order order) {
        this.order = order;

        do {
            p.menu("REASSIGN ORDER TO EMPLOYEE");
            orderService.assignOrderToEmployeeByListChoice(order);
        } while (p.promptContinue("Reassign another order"));
    }

}
