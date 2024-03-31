package org.example.models;

import lombok.*;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

import java.math.BigDecimal;

@Table(name = "order_types")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OrderType {

    @PrimaryKey
    private Integer orderTypeId;
    private String orderType;
    private BigDecimal price;

    public OrderType(String orderType, BigDecimal price) {
        this.orderType = orderType;
        this.price = price;
    }
}
