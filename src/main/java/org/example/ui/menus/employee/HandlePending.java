package org.example.ui.menus.employee;

import org.example.models.Employee;
import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Login;

import java.util.List;

public class HandlePending extends Login<Employee> {

    OrderService orderService = new OrderService();

    List<Order> pendingOrders;

    public void run(Employee currentUser) {
        this.currentUser = currentUser;

        do {
            pendingOrders = orderService.getPendingOrders();
            if (pendingOrders.isEmpty()) return;

            p.menu("PENDING CASES");
            var index = u.printChoosingList(pendingOrders);
            if (index == null) return;
            if (!p.promptContinue("TAKE CASE?")) return;

            assignOrderToEmployee(pendingOrders.get(index));

        } while (p.promptContinue("Take another case?"));
    }

    private void assignOrderToEmployee(Order order) {
        var updatedOrder = orderService.assignOrderByEmployeeId(order, currentUser.getEmployeeId());
        p.printMessage("Order " + updatedOrder.getOrderNo() + " assigned!");
    }

}
