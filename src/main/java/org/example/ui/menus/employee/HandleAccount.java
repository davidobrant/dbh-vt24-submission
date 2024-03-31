package org.example.ui.menus.employee;

import org.example.forms.EmployeeForm;
import org.example.models.Employee;
import org.example.supers.Login;
import org.example.supers.Controls;

public class HandleAccount extends Login<Employee> {

    Controls controls;

    public void run(Employee currentUser) {
        this.currentUser = currentUser;

        controls = new Controls();
        do menu(); while (controls.isRunning());
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
            case "exit", "0" -> controls.exit();
            case "1", "print" -> print();
            case "2", "update" -> update();
            default -> p.invalidCommand();
        }
    }

    private void print() {
        p.printH1("ACCOUNT INFORMATION");
        p.printEntity(currentUser);
    }

    private void update() {
        p.menu("UPDATE ACCOUNT");
        new EmployeeForm().update(currentUser);
    }
}
