package org.example.ui.menus.admin.orders.list.handler;

import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Menu;

public class HandlePending extends Menu {

    OrderService orderService = new OrderService();
    Order order;

    public HandlePending(Order order) {
        this.order = order;

        do {
            p.menu("ASSIGN ORDER TO EMPLOYEE");
            orderService.assignOrderToEmployeeByListChoice(order);
        } while (p.promptContinue("Assign another order"));
    }
    
}
