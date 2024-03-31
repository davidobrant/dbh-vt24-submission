package org.example.ui.menus;

import org.example.models.Customer;
import org.example.ui.menus.customer.HandleInvoices;
import org.example.supers.Login;
import org.example.ui.menus.customer.HandleAccount;
import org.example.ui.menus.customer.HandleOrders;
import org.example.ui.menus.customer.HandleProducts;

public class CustomerMenu extends Login<Customer> {

    HandleAccount account = new HandleAccount();
    HandleProducts products = new HandleProducts();
    HandleOrders orders = new HandleOrders();
    HandleInvoices invoices = new HandleInvoices();


    public void run() {
        if (!login(Customer.class)) return;
        super.run();

        while (running) customerMenu();
    }

    private void customerMenu() {
        p.menu("CUSTOMER MENU");
        System.out.print("""
            [1] My Account
            [2] My Phones
            [3] My Cases
            [4] My Invoices
            [0] Logout
            """);
        handleCustomerMenu(p.promptCommand());
    }

    private void handleCustomerMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> logout();
            case "1", "account" -> account.run(currentUser);
            case "2", "phones" -> products.run(currentUser);
            case "3", "orders" -> orders.run(currentUser);
            case "4", "invoices" -> invoices.run(currentUser);
            default -> p.invalidCommand();
        }
    }

}
