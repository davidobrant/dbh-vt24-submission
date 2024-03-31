package org.example.ui.menus.admin.orders;

import org.example.services.OrderService;
import org.example.supers.Menu;
import org.example.ui.menus.admin.orders.list.handler.HandleOrder;

public class FindOrder extends Menu {

    OrderService orderService = new OrderService();

    public void run() {
        super.run();

        while (running) menu();
    }

    private void menu() {
        p.menu("FIND ORDER BY");
        System.out.println("""
                        [1] ID
                        [2] Order Number
                        [0] Exit
                        """
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "0", "exit" -> exit();
            case "1", "id" -> findById();
            case "2", "number" -> findByOrderNo();
            default -> p.invalidCommand();
        }
    }

    private void findById() {
        var orderId = p.promptInt("Order ID");
        var order = orderService.getOrderById(orderId);
        if (order == null) {
            p.printMessage("No order found by ID: " + orderId);
        }
        p.printEntity(order);
        new HandleOrder().run(order);
    }

    private void findByOrderNo() {
        var orderNo = p.prompt("Order Number");
        var order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            p.printMessage("No order found by number: " + orderNo);
        }
        p.printEntity(order);
        new HandleOrder().run(order);
    }

}
