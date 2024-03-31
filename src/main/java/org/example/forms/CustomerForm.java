package org.example.forms;

import org.example.models.Customer;
import org.example.services.CustomerService;
import org.example.supers.Controls;
import org.example.supers.Utils;

public class CustomerForm extends Utils {

    CustomerService customerService = new CustomerService();

    public Customer create() {
        var customerName = p.prompt("NAME");
        var email = promptEmail("EMAIL");
        var phone = p.prompt("PHONE");
        var address = p.prompt("ADDRESS");
        return new Customer(customerName, email, phone, address);
    }

    public Customer update(Customer customer) {
        Controls controls = new Controls();
        while (controls.isRunning()) {
            System.out.print(
            "[1] Name " + f.parentheses(customer.getCustomerName()) + "\n" +
            "[2] Email " +  f.parentheses(customer.getEmail()) + "\n" +
            "[3] Phone " + f.parentheses(customer.getPhone()) + "\n" +
            "[4] Address " + f.parentheses(customer.getAddress()) + "\n" +
            "[0] Exit\n"
            );

            String cmd = p.promptCommand();

            switch (cmd) {
                case "exit", "0" -> {
                    controls.exit();
                }
                case "1", "name" -> {
                    var customerName = p.prompt("NAME");
                    customer.setCustomerName(customerName);
                    customerService.updateCustomer(customer);
                }
                case "2", "email" -> {
                    var email = promptEmail("EMAIL");
                    customer.setEmail(email);
                    customerService.updateCustomer(customer);
                }
                case "3", "phone" -> {
                    var phone = p.prompt("PHONE");
                    customer.setPhone(phone);
                    customerService.updateCustomer(customer);
                }
                case "4", "address" -> {
                    var address = p.prompt("ADDRESS" );
                    customer.setAddress(address);
                    customerService.updateCustomer(customer);
                }
                default -> p.invalidCommand();
            }
        }
        return customer;
    }

    private String promptEmail(String value) {
        try {
            var email = p.prompt(value);
            if (customerService.getCustomerByEmail(email) != null) {
                throw new Exception("Email already exists.");
            }
            return email;
        } catch (Exception e) {
            p.printError(e.getMessage());
            return promptEmail(value);
        }
    }

}
