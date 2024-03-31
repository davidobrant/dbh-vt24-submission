package org.example.models;


import lombok.*;
import org.example.annotations.ForeignKey;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

@Table(name = "order_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderImage {

    @PrimaryKey
    private Integer orderImageId;
    @ForeignKey(table = "orders", field = "order_id")
    private Integer orderId;
    private String imageKey;

    public OrderImage(Integer orderId, String imageKey) {
        this.orderId = orderId;
        this.imageKey = imageKey;
    }
}
