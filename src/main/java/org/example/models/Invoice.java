package org.example.models;

import lombok.*;
import org.example.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Invoice {

    @PrimaryKey
    private Integer invoiceId;
    @IdentityNo(identifier = "999")
    private String invoiceNo;
//    @ForeignKey(table = "orders", field = "order_no")
    private String orderNo;
    @ForeignKey(table = "customers", field = "customer_id")
    private int customerId;
    @AutoExclude
    @ForeignKey(table = "invoice_statuses", field = "invoice_status_id")
    private int invoiceStatusId;
    private BigDecimal amount;
    @AutoExclude
    private Timestamp invoiceDate;
    @AutoExclude
    private Timestamp paymentDate;

    public Invoice(String orderNo, Integer customerId, BigDecimal amount) {
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.amount = amount;
    }
}
