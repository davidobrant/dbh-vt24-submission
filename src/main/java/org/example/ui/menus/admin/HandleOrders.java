package org.example.ui.menus.admin;

import org.example.supers.Menu;
import org.example.ui.menus.admin.orders.FindOrder;
import org.example.ui.menus.admin.orders.ListOrders;


public class HandleOrders extends Menu {

    public void run() {
        super.run();

        while (running) menu();
    }

    private void menu() {
        p.menu("ORDERS");
        System.out.print("""
        [1] List
        [2] Find One
        [0] Exit
        """);
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "list" -> new ListOrders().run();
            case "2", "find" -> new FindOrder().run();
            default -> p.invalidCommand();
        }
    }

}
