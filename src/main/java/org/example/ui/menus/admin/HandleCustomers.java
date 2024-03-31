package org.example.ui.menus.admin;

import org.example.forms.CustomerForm;
import org.example.models.Customer;
import org.example.services.CustomerService;
import org.example.supers.Menu;

import java.util.NoSuchElementException;

public class HandleCustomers extends Menu {

    CustomerService customerService = new CustomerService();

    public void run() {
        super.run();

        while (running) menu();
    }

    private void menu() {
        p.menu("CUSTOMERS");
        System.out.print("""
            [1] List
            [2] Find One
            [3] Add new
            [4] Update
            [5] Delete
            [0] Exit
            """);
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "list" -> list();
            case "2", "find" -> findOne();
            case "3", "create" -> create();
            case "4", "update" -> update();
            case "5", "delete" -> delete();
            default -> p.invalidCommand();
        }
    }

    private void list() {
        p.printH1("ALL CUSTOMERS");
        var customers = customerService.getAllCustomers();
        p.printCustomers(customers);
    }
    private void findOne() {
        p.menu("FIND CUSTOMER");
        var customer = promptCustomerById();
        p.printCustomer(customer);
    }
    private void create() {
        p.menu("CREATE CUSTOMER");
        var newCustomer = new CustomerForm().create();
        var customer = customerService.addCustomer(newCustomer);
        p.printCustomer(customer);
    }
    private void update() {
        p.menu("UPDATE CUSTOMER");
        var customer = promptCustomerById();
        if (customer == null) return;
        var updatedCustomer = new CustomerForm().update(customer);
        p.printCustomer(updatedCustomer);
    }
    private void delete() {
        p.menu("DELETE CUSTOMER");
        var customer = promptCustomerById();
        if (customer == null) return;
        var customerId = customer.getCustomerId();
        if (p.promptContinue("DELETE Customer with ID: " + customerId)) {
            customerService.deleteCustomerById(customerId);
        }
    }

    private Customer promptCustomerById() {
        try {
            var customerId = p.promptInt("Customer ID");
            System.out.println();
            if (customerId == 0) return null;
            return customerService.getCustomerById(customerId);
        } catch (NoSuchElementException e) {
            p.noResultsFound();
            return null;
        }
    }
}
