package org.example.ui.menus;

import org.example.models.Employee;
import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Login;
import org.example.ui.menus.employee.HandleAccount;
import org.example.ui.menus.employee.HandleAssigned;
import org.example.ui.menus.employee.HandlePending;

import java.util.List;

public class EmployeeMenu extends Login<Employee> {


    HandleAccount account = new HandleAccount();
    HandleAssigned assigned = new HandleAssigned();
    HandlePending pending = new HandlePending();

    OrderService orderService = new OrderService();

    List<Order> assignedOrders;
    List<Order> pendingOrders;
    List<Order> closedOrders;

    public void run() {
        if (!login(Employee.class)) return;
        super.run();

        while (running) {
            this.assignedOrders = orderService.getAssignedOrdersByEmployeeId(currentUser.getEmployeeId());
            this.pendingOrders = orderService.getPendingOrders();
            this.closedOrders = orderService.getClosedOrdersByEmployeeId(currentUser.getEmployeeId());
            employeeMenu();
        }
    }

    private void employeeMenu() {
        p.menu("EMPLOYEE MENU");
        System.out.print(
            "[1] My Account\n" +
            "[2] Assigned Cases (" + assignedOrders.size() + ")\n" +
            "[3] Pending Cases (" + pendingOrders.size() + ")\n" +
            "[4] Closed Cases (" + closedOrders.size() + ")\n" +
            "[0] Logout\n"
        );
        handleEmployeeMenu(p.promptCommand());
    }

    private void handleEmployeeMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> logout();
            case "1", "account" -> account.run(currentUser);
            case "2", "assigned" -> {
                if (assignedOrders.isEmpty()) {
                    p.printMessage("No assigned cases. Check pending!");
                    return;
                }
                assigned.run(currentUser);
            }
            case "3", "pending" -> {
                if (pendingOrders.isEmpty()) {
                    p.printMessage("No pending cases. Take a load off, dude!");
                    return;
                }
                pending.run(currentUser);
            }
            case "4", "closed" -> {
                if (closedOrders.isEmpty()) {
                    p.printMessage("No closed cases. Get to work, buddy!");
                    return;
                }
                p.printEntities(closedOrders);
            }
            default -> p.invalidCommand();
        }
    }

}
