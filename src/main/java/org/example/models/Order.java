package org.example.models;

import lombok.*;
import org.example.annotations.*;

import java.sql.Date;
import java.sql.Timestamp;

@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Order {

    @PrimaryKey
    private Integer orderId;
    @IdentityNo(identifier = "777")
    private String orderNo;
    @ForeignKey(table = "employees", field = "employee_id")
    private Integer employeeId;
    @ForeignKey(table = "customers", field = "customer_id")
    private Integer customerId;
    @ForeignKey(table = "products", field = "product_id")
    private Integer productId;
    @AutoExclude
    @ForeignKey(table = "order_statuses", field = "order_status_id")
    private Integer orderStatusId;
    @ForeignKey(table = "order_types", field = "order_type_id")
    private Integer orderTypeId;
    private String description;
    private String notes;
    private boolean hasAttachments;
    @AutoExclude
    private Timestamp orderDate;
    @AutoExclude
    private Timestamp updatedAt;

    public Order(Integer customerId, Integer productId, Integer orderTypeId, String description) {
        this.customerId = customerId;
        this.productId = productId;
        this.orderTypeId = orderTypeId;
        this.description = description;
    }

    public Order(Integer customerId, Integer productId, Integer orderStatusId, Integer orderTypeId, String description) {
        this.customerId = customerId;
        this.productId = productId;
        this.orderStatusId = orderStatusId;
        this.orderTypeId = orderTypeId;
        this.description = description;
    }

    public boolean getHasAttachments() {
        return hasAttachments;
    }
}
