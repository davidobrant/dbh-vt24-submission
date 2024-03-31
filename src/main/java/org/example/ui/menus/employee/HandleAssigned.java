package org.example.ui.menus.employee;

import org.example.models.Employee;
import org.example.models.Order;
import org.example.s3.S3Repository;
import org.example.services.OrderService;
import org.example.supers.Login;
import org.example.supers.Controls;

import java.util.List;

public class HandleAssigned extends Login<Employee> {

    OrderService orderService = new OrderService();

    List<Order> assignedOrders;


    public void run(Employee currentUser) {
        this.currentUser = currentUser;
        super.run();

        do {
            assignedOrders = orderService.getAssignedOrdersByEmployeeId(currentUser.getEmployeeId());
            if (assignedOrders.isEmpty()) return;

            p.menu("MY CASES");

            var index = u.printChoosingList(assignedOrders);
            if (index == null) return;

            handleOrder(assignedOrders.get(index));
        } while (running);
    }

    private void handleOrder(Order order) {
        controls = new Controls();
        boolean hasAttachments = order.getHasAttachments();

        do {
            p.menu("ORDER " + order.getOrderNo());
            System.out.print(
                    "[1] View Case\n" +
                    "[2] Add notes\n" +
                    "[3] Close Case\n" +
                    "[4] Unassign Case\n" +
                    (hasAttachments ? "[5] List Attachments\n" : "") +
                    (hasAttachments ? "[6] Download Attachments\n" : "") +
                    "[0] Exit\n"
            );
            handleCommand(p.promptCommand(), order);
        } while (controls.isRunning());

    }

    private void handleCommand(String cmd, Order order) {
        boolean hasAttachments = order.getHasAttachments();
        switch (cmd) {
            case "0", "exit" -> controls.exit();
            case "1", "view" -> view(order);
            case "2", "notes" -> notes(order);
            case "3", "close" -> close(order);
            case "4", "unassign" -> unAssign(order);
            case "5", "list" -> {
                if (hasAttachments) {
                    list(order);
                }
            }
            case "6", "download" -> {
                if (hasAttachments) {
                    download(order);
                }
            }
            default -> p.invalidCommand();
        }
    }

    private void view(Order order) {
        p.printEntity(order);
    }

    private void notes(Order order) {
        var notes = p.prompt("NOTES");
        order.setNotes(notes);
        orderService.updateOrder(order);
    }

    private void close(Order order) {
        var invoice = orderService.closeOrder(order);
        p.printMessage("Order " + order.getOrderNo() + " closed.");
        p.printEntity(invoice);
        controls.exit();
    }

    private void unAssign(Order order) {
        var updatedOrder = orderService.unAssignOrder(order);
        if (updatedOrder.getEmployeeId() != null) {
            p.printError("Sorry, you're stuck with this one...");
        } else {
            p.printMessage("Order " + order.getOrderNo() + " unassigned.");
        }
    }

    private void list(Order order) {
        new S3Repository().listS3Folder(order.getOrderNo());
    }

    private void download(Order order) {
        try {
            var paths = new S3Repository().downloadFolder(order.getOrderNo());
            p.printMessage("DOWNLOAD COMPLETE\n");
            paths.forEach(p::printLink);
            p.printMessage("\n> View files in ./assets/downloads/" + order.getOrderNo() + "\n" );
        } catch (Exception e) {
            p.printError("Error: " + e.getMessage());
        }
    }


}
