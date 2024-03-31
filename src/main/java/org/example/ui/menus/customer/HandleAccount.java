package org.example.ui.menus.customer;

import org.example.forms.CustomerForm;
import org.example.models.Customer;
import org.example.supers.Login;

public class HandleAccount extends Login<Customer> {


    public void run(Customer currentUser) {
        this.currentUser = currentUser;
        super.run();
        while (running) menu();
    }

    public void menu() {
        p.menu("ACCOUNT");
        System.out.print("""
            [1] Print Information
            [2] Update
            [0] Exit
            """);
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "print" -> print();
            case "2", "update" -> update();
            default -> p.invalidCommand();
        }
    }

    private void print() {
        p.printH1("ACCOUNT INFORMATION");
        p.printCustomer(currentUser);
    }

    private void update() {
        p.menu("UPDATE ACCOUNT");
        new CustomerForm().update(currentUser);
    }
}
