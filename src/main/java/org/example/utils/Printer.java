package org.example.utils;

import org.example.models.Customer;
import org.example.models.Employee;
import org.example.models.Product;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class Printer {

    Scanner scanner = new Scanner(System.in);
    Formatter f = new Formatter();


    /* ----- PROMPT ----- */
    public String promptCommand() {
        System.out.print(f.prompt("command"));
        return scanner.nextLine().toLowerCase();
    }

    public String prompt(String value) {
        System.out.print(f.prompt(value));
        String res = scanner.nextLine();
        if (res.isEmpty()) return prompt(value);
        return res;
    }

    public int promptInt(String value) {
        while (true) {
            try {
                System.out.print(f.prompt(value));
                String cmd = scanner.nextLine();
                if (cmd.equalsIgnoreCase("exit") | cmd.equals("0")) return -1;
                return Integer.parseInt(cmd);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input... Please enter a number!");
            }
        }
    }

    public boolean promptContinue(String value) {
        System.out.print("\n"+value + " (y/N)?: ");
        String cmd = scanner.nextLine();
        return cmd.equalsIgnoreCase("y") | cmd.equalsIgnoreCase("yes");
    }

    public void promptEnter() {
        System.out.print("\n> Press \"ENTER\" to continue: ");
        scanner.nextLine();
    }

    public void printLink(String path) {
        System.out.println(path);
    }
    /* --x-- PROMPT --x-- */
    /* ----- PRINT ----- */
    public void invalidCommand() {
        System.out.println("Invalid command... Please try again!");
    }
    public void noResultsFound() {
        System.out.println("No results found...");
    }

    public void printError(String message) {
        System.out.println(message);
    }
    public void printMessage(String message) {
        System.out.println(message);
    }
    /* --x-- PRINT --x-- */

    /* ----- HEADING ----- */
    public void printH1(String heading) {
        System.out.println(f.h1(heading));
    }
    public void printH2(String heading) {
        System.out.println(f.h2(heading));
    }
    /* --x-- HEADING --x-- */
    /* ----- MENU ----- */
    public void menu(String title) {
        System.out.println(f.menu(title));
    }
    /* --x-- MENU --x-- */
    /* ----- ENTITIES ----- */
    public void printCustomer(Customer customer) {
        System.out.println(customer);
    }

    public void printCustomers(List<Customer> customers) {
        for (var customer : customers) {
            printCustomer(customer);
        }
    }
    public void printEmployee(Employee employee) {
        System.out.println(employee);
    }

    public void printEmployees(List<Employee> employees) {
        for (var employee : employees) {
            printEmployee(employee);
        }
    }
    public void printProduct(Product product) {
        System.out.println(product);
    }

    public void printProducts(List<Product> products) {
        for (var product : products) {
            printProduct(product);
        }
    }

    public <T> void printEntity(T entity) {
        System.out.println(entity);
    }

    public <T> void printEntities(List<T> entities) {
        if (entities.isEmpty()) {
            printMessage("List is empty...");
        }
        for (var entity : entities) {
            printEntity(entity);
        }
    }


    /* --x-- ENTITIES --x-- */

}
