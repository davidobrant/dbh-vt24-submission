package org.example.models;

import lombok.*;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

@Table(name = "order_statuses")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OrderStatus {

    @PrimaryKey
    private Integer orderStatusId;
    private String orderStatus;

    public OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
