package org.example.ui.menus.admin.orders.list.handler;

import org.example.forms.OrderForm;
import org.example.models.Order;
import org.example.services.OrderService;
import org.example.supers.Menu;

public class HandleOrder extends Menu {

    OrderService orderService = new OrderService();
    Order order;

    public void run(Order order) {
        this.order = order;
        super.run();

        while (running) menu();
    }

    private void menu() {
        p.menu("HANDLE ORDER");
        System.out.println("""
                        [1] Complete
                        [2] Assign
                        [3] Update
                        [4] Delete
                        [0] Exit
                        """
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "0", "exit" -> exit();
            case "1", "complete" -> complete();
            case "2", "assign" -> assign();
            case "3", "update" -> update();
            case "4", "delete" -> delete();
            default -> p.invalidCommand();
        }
    }

    private void complete() {
        p.menu("COMPLETE ORDER");
        var invoice = orderService.closeOrder(order);
        p.printMessage("Order " + order.getOrderNo() + " closed.");
        p.printEntity(invoice);
    }

    private void assign() {
        p.menu("ASSIGN ORDER TO EMPLOYEE");
        orderService.assignOrderToEmployeeByListChoice(order);
    }

    private void update() {
        p.menu("UPDATE ORDER");
        new OrderForm().update(order);
    }

    private void delete() {
        p.menu("DELETE ORDER");
        if (orderService.deleteOrder(order)) {
            p.printMessage("Order deleted.");
        } else {
            p.printError("Error deleting order.");
        }
    }
}
