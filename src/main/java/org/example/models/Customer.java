package org.example.models;

import lombok.*;
import org.example.annotations.IdentityNo;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Referenced;
import org.example.annotations.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Referenced(tables = {"products"})
@Table(name = "customers")
public class Customer {

    @PrimaryKey
    private Integer customerId;
    @IdentityNo(identifier = "111")
    private String customerNo;
    private String customerName;
    private String email;
    private String phone;
    private String address;

    public Customer(String customerName, String email, String phone, String address) {
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
