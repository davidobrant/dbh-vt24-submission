package org.example.models;

import lombok.*;
import org.example.annotations.ForeignKey;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "products")
public class Product {

    @PrimaryKey
    private Integer productId;
    private String brand;
    private String model;
    private Integer productionYear;

    @ForeignKey(table = "customers", field = "customer_id")
    private Integer customerId;

    public Product(String brand, String model, Integer productionYear, Integer customerId) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.customerId = customerId;
    }

    public Product(String brand, String model, Integer productionYear) {
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
    }
}
