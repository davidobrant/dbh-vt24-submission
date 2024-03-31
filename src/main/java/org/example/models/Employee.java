package org.example.models;

import lombok.*;
import org.example.annotations.IdentityNo;
import org.example.annotations.PrimaryKey;
import org.example.annotations.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "employees")
public class Employee {

    @PrimaryKey
    private Integer employeeId;
    @IdentityNo(identifier = "333")
    private String employeeNo;
    private String employeeName;
    private String email;
    private String phone;

    public Employee(String employeeName, String email, String phone) {
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
    }
}
