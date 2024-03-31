package org.example.services;

import org.example.models.*;
import org.example.repositories.OrderRepository;
import org.example.supers.Utils;

import java.util.List;

public class OrderService extends Utils {

    OrderRepository orderRepository = new OrderRepository();
    EmployeeService employeeService;


    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.getOrderByOrderNo(orderNo);
    }

    public List<Order> getPendingOrders() {
        return orderRepository.getAllOrders().stream()
                .filter(order -> order.getOrderStatusId() == 1)
                .toList();
    }

    public Order assignOrderByEmployeeId(Order order, Integer employeeId) {
        order.setOrderStatusId(2);
        order.setEmployeeId(employeeId);
        return orderRepository.updateOrder(order);
    }

    public Order unAssignOrder(Order order) {
        order.setOrderStatusId(1);
        order.setEmployeeId(null);
        return orderRepository.updateOrder(order);
    }

    public List<Order> getAssignedOrdersByEmployeeId(Integer employeeId) {
        return orderRepository.getAllByEmployeeId(employeeId).stream()
                .filter(order -> order.getOrderStatusId() == 2)
                .toList();
    }
    public List<Order> getActiveOrdersByCustomerId(Integer customerId) {
        return orderRepository.getAllByCustomerId(customerId).stream()
                .filter(order -> order.getOrderStatusId() != 3)
                .toList();
    }
    public List<Order> getClosedOrdersByCustomerId(Integer customerId) {
        return orderRepository.getAllByCustomerId(customerId).stream()
                .filter(order -> order.getOrderStatusId() == 3)
                .toList();
    }

    public List<Order> getClosedOrdersByEmployeeId(Integer employeeId) {
        return orderRepository.getAllByEmployeeId(employeeId).stream()
                .filter(order -> order.getOrderStatusId() == 3)
                .toList();
    }

    public Order addOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    public Order updateOrder(Order order) {
        return orderRepository.updateOrder(order);
    }

    public Invoice closeOrder(Order order) { return orderRepository.closeOrder(order);  }

    public List<OrderType> getAllOrderTypes() {
        return orderRepository.getAllOrderTypes();
    }

    public List<OrderStatus> getAllOrderStatuses() {
        return orderRepository.getAllOrderStatuses();
    }

    public boolean existsInActiveOrders(Integer productId, Integer customerId) {
        return getActiveOrdersByCustomerId(customerId).stream()
                .anyMatch(order -> order.getProductId().equals(productId));
    }

    public OrderImage addOrderImage(OrderImage image) {
        return orderRepository.addOrderImage(image);
    }

    public void assignOrderToEmployeeByListChoice(Order order) {
        employeeService = new EmployeeService();
        var employee = employeeService.chooseEmployeeByListChoice();
        if (employee == null) {
            p.printError("No employee found...");
            return;
        }
        var assignOrder = assignOrderByEmployeeId(order, employee.getEmployeeId());
        p.printH1("Order " + order.getOrderNo() + " assigned to employee " + employee.getEmployeeNo());
        p.printEntity(assignOrder);
    }

    public boolean deleteOrder(Order order) {
        return orderRepository.deleteOrder(order);
    }

    public OrderStatus getOrderStatusById(Integer orderStatusId) {
        return orderRepository.getOrderStatusById(orderStatusId);
    }

    public OrderType getOrderTypeById(Order order) {
        return orderRepository.getOrderType(order);
    }
}
