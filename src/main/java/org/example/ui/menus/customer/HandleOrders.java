package org.example.ui.menus.customer;

import org.example.forms.OrderForm;
import org.example.models.Customer;
import org.example.models.Order;
import org.example.models.OrderImage;
import org.example.s3.S3Repository;
import org.example.services.OrderService;
import org.example.supers.Login;

import java.util.ArrayList;
import java.util.List;

public class HandleOrders extends Login<Customer> {

    OrderService orderService = new OrderService();

    List<Order> activeOrders;
    List<Order> closedOrders;

    public void run(Customer currentUser) {
        this.currentUser = currentUser;
        super.run();

        while (running) menu();
    }

    private void menu() {
        fetchOrders();
        p.menu("MY CASES");
        System.out.print(
                "[1] New Case \n" +
                "[2] Active (" + activeOrders.size() + ")\n" +
                "[3] History (" + closedOrders.size() + ")\n" +
                "[0] Exit\n"
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "new" -> newCase();
            case "2", "active" -> p.printEntities(activeOrders);
            case "3", "history" -> p.printEntities(closedOrders);
            default -> p.invalidCommand();
        }
    }

    private void newCase() {
        p.menu("NEW CASE");
        var order = new OrderForm().createAuthId(currentUser.getCustomerId());
        if (order == null) return;

        if (p.promptContinue("ADD IMAGE TO CASE")) {
            S3Repository s3Repository = new S3Repository();
            List<OrderImage> orderImages = new ArrayList<>();

            do {
                var s3key = s3Repository.upload(order);
                var orderImage = orderService.addOrderImage(new OrderImage(order.getOrderId(), s3key));
                orderImages.add(orderImage);
            } while (p.promptContinue("ADD ANOTHER IMAGE"));

            order.setHasAttachments(true);
            orderService.updateOrder(order);

            p.printMessage(orderImages.size() + " images added successfully.");
        }

        p.printH1("CASE ORDER CREATED");
        p.printEntity(order);
    }

    private void fetchOrders() {
        activeOrders = orderService.getActiveOrdersByCustomerId(currentUser.getCustomerId());
        closedOrders = orderService.getClosedOrdersByCustomerId(currentUser.getCustomerId());
    }
}
