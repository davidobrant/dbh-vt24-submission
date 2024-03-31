package org.example.forms;

import org.example.models.Customer;
import org.example.models.Order;
import org.example.services.OrderService;
import org.example.services.ProductService;
import org.example.supers.Controls;
import org.example.supers.Utils;


public class OrderForm extends Utils {

    OrderService orderService = new OrderService();
    ProductService productService = new ProductService();

    public Order createAuthId(Integer customerId) {
        var productId = promptProductIdAuthId(customerId);
        if (productId == null) return null;
        var orderTypeId = promptOrderTypeId();
        var description = p.prompt("DESCRIPTION");
        return orderService.addOrder(new Order(customerId, productId, 1, orderTypeId,  description));
    }

    private Integer promptProductIdAuthId(Integer customerId) {
        try {
            if (!(productService.getProductCountByCustomerId(customerId) > 0)) {
                throw new Exception("You have no registered Phones. Go to 'My Phones' to add one.");
            }
            p.menu("CHOOSE DEVICE");
            var products = productService.getProductsNotInActiveOrderByCustomer(customerId);
            if (products.isEmpty()) {
                throw new Exception("There are active orders on all your Phones.");
            }

            var index = u.printChoosingList(products);
            if (index == null) {
                return null;
            }

            var productId = products.get(index).getProductId();

            if (orderService.existsInActiveOrders(customerId, productId)) {
                throw new Exception("There is already an active order on this Phone.");
            }

            return productId;
        } catch (Exception e) {
            p.printError(e.getMessage());
            return null;
        }
    }

    private Integer promptOrderTypeId() {
        try {
            p.menu("CHOOSE CASE TYPE");
            var orderTypes = orderService.getAllOrderTypes();
            if (orderTypes.isEmpty()) {
                throw new Exception("Something went wrong...");
            }

            var index = u.printChoosingList(orderTypes);

            if (index == null) {
                throw new Exception("Something went wrong...");
            }

            return orderTypes.get(index).getOrderTypeId();
        } catch (Exception e) {
            p.printError(e.getMessage());
            return null;
        }
    }

    public Order update(Order order) {
        Controls controls = new Controls();

        var id = "ORDER ID: " + order.getOrderId();
        var no = ", ORDER NO: " + order.getOrderNo();
        var customer = ", CUSTOMER ID: " + order.getCustomerId();
        var product = ", PRODUCT ID: " + order.getProductId();

        while (controls.isRunning()) {
            var statusString = orderService.getOrderStatusById(order.getOrderStatusId()).getOrderStatus();
            var typeString = orderService.getOrderTypeById(order).getOrderType();
            var notesString = order.getNotes();

            p.menu(id + no + customer + product);
            System.out.print(
                    "[1] Status " +  f.parentheses(statusString) + "\n" +
                    "[2] Type " +  f.parentheses(typeString) + "\n" +
                    "[3] Notes " + f.parentheses(notesString) + "\n" +
                    "[0] Exit\n"
            );

            String cmd = p.promptCommand();

            switch (cmd) {
                case "exit", "0" -> {
                    controls.exit();
                }
                case "1", "status" -> {
                    var orderStatuses = orderService.getAllOrderStatuses();
                    var index = u.printChoosingList(orderStatuses);
                    order.setOrderStatusId(orderStatuses.get(index).getOrderStatusId());
                }
                case "2", "type" -> {
                    var orderTypes = orderService.getAllOrderTypes();
                    var index = u.printChoosingList(orderTypes);
                    order.setOrderTypeId(orderTypes.get(index).getOrderTypeId());
                }
                case "3", "notes" -> {
                    var notes = p.prompt("NOTES");
                    order.setNotes(notes);
                }
                default -> p.invalidCommand();
            }
            orderService.updateOrder(order);
        }
        return order;
    }

}
