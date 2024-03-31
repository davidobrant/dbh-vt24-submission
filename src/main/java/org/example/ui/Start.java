package org.example.ui;

import org.example.init.Setup;
import org.example.supers.Menu;
import org.example.ui.menus.AdminMenu;
import org.example.ui.menus.CustomerMenu;
import org.example.ui.menus.EmployeeMenu;
import org.example.ui.menus.SettingsMenu;

import java.sql.SQLException;

public class Start extends Menu {

    private final CustomerMenu customerMenu = new CustomerMenu();
    private final EmployeeMenu employeeMenu = new EmployeeMenu();
    private final AdminMenu adminMenu = new AdminMenu();
    private final SettingsMenu settingsMenu = new SettingsMenu();

    public Start() throws SQLException {
        super();
        new Setup();
        startMenu();
    }

    private void startMenu() throws SQLException {
        while (running) {
            p.menu("START");
            System.out.print("""
                Login as:
                [1] Customer
                [2] Employee
                [3] Admin
                [4] Settings
                [0] Exit
                """);
            handleStartMenu(p.promptCommand());
        }
    }

    private void handleStartMenu(String cmd) throws SQLException {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1" -> customerMenu.run();
            case "2" -> employeeMenu.run();
            case "3" -> adminMenu.run();
            case "4" -> settingsMenu.run();
            default -> p.invalidCommand();
        }
    }

}
