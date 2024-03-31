package org.example.ui.menus;

import org.example.supers.Menu;
import org.example.ui.menus.admin.*;
import org.example.supers.Controls;
import org.example.utils.Printer;

public class AdminMenu extends Menu {


    private final HandleCustomers customers = new HandleCustomers();
    private final HandleEmployees employees = new HandleEmployees();
    private final HandleProducts products = new HandleProducts();
    private final HandleOrders orders = new HandleOrders();
    private final HandleInvoices invoices = new HandleInvoices();

    public void run() {
        super.run();

        while (running) adminMenu();
    }

    private void adminMenu() {
        p.menu("ADMIN MENU");
        System.out.print("""
            [1] Customers
            [2] Employees
            [3] Products
            [4] Orders
            [5] Invoices
            [0] Exit
            """);
        handleAdminMenu(p.promptCommand());
    }

    private void handleAdminMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "customers" -> customers.run();
            case "2", "employees" -> employees.run();
            case "3", "products" -> products.run();
            case "4", "orders" -> orders.run();
            case "5", "invoices" -> invoices.run();
            default -> p.invalidCommand();
        }
    }



}
