package org.example.repositories;

import org.example.models.*;
import org.example.s3.S3Repository;
import org.example.services.InvoiceService;

import java.sql.SQLException;
import java.util.List;


public class OrderRepository extends BaseRepository {

    InvoiceService invoiceService;
    S3Repository s3Repository;

    public List<Order> getAllOrders() {
        return findAll(Order.class);
    }

    public Order getOrderById(Integer orderId) {
        return findById(Order.class, orderId);
    }

    public Order getOrderByOrderNo(String orderNo) {
        return findByIdentityNo(Order.class, orderNo);
    }

    public List<Order> getAllByCustomerId(Integer customerId) {
        return findAllByForeignKey(Order.class, Customer.class, customerId);
    }

    public List<Order> getAllByEmployeeId(Integer employeeId) {
        return findAllByForeignKey(Order.class, Employee.class, employeeId);
    }

    public Order createOrder(Order order) {
        return create(Order.class, order);
    }

    public Order updateOrder(Order order) { return update(Order.class, order); }

    public OrderImage addOrderImage(OrderImage image) { return create(OrderImage.class, image); }

    public void deleteOrderImage(OrderImage image) {
        deleteById(OrderImage.class, image.getOrderImageId());
    }

    public List<OrderImage> getAllOrderImagesByOrderId(Integer orderId) {
        return findAllByForeignKey(OrderImage.class, Order.class, orderId);
    }

    public List<OrderType> getAllOrderTypes() { return findAll(OrderType.class); }

    public List<OrderStatus> getAllOrderStatuses() { return findAll(OrderStatus.class); }

    public OrderType getOrderType(Order order) {
        return findById(OrderType.class, order.getOrderTypeId());
    }

    public Invoice closeOrder(Order order) {
        try {
            if (order.getOrderStatusId() == 3) throw new Exception("Order is already closed.");

            invoiceService = new InvoiceService();
            s3Repository = new S3Repository();

            conn.setAutoCommit(false);

            order.setOrderStatusId(3);

            var price = getOrderType(order).getPrice();
            var invoice = invoiceService.createInvoice(order, price);

            if (order.getHasAttachments()) {

                for (var image : getAllOrderImagesByOrderId(order.getOrderId())) {
                    deleteOrderImage(image);
                }

                s3Repository.deleteS3Folder(order.getOrderNo());
                s3Repository.deleteLocalOrderImagesFolder(order.getOrderNo());

                order.setHasAttachments(false);
            }

            updateOrder(order);

            conn.commit();
            conn.setAutoCommit(true);

            return invoice;
        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                p.printError("Error: " + e.getMessage());
            }
            p.printError("Error: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            p.printError("Error: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteOrder(Order order) {
        return deleteById(Order.class, order.getOrderId());
    }

    public OrderStatus getOrderStatusById(Integer orderStatusId) {
        return findById(OrderStatus.class, orderStatusId);
    }
}
