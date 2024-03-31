package org.example.models;

import lombok.*;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

@Table(name = "invoice_statuses")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class InvoiceStatus {

    @PrimaryKey
    private Integer invoiceStatusId;
    private String invoiceStatus;

    public InvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
