package org.example.ui.menus.admin;

import org.example.services.ProductService;
import org.example.supers.Controls;
import org.example.utils.Printer;

public class HandleProducts {

    Controls controls;
    Printer p = new Printer();
    private final ProductService productService = new ProductService();

    public void run() {
        controls = new Controls();
        while (controls.isRunning()) {
            menu();
        }
    }

    private void menu() {
        p.menu("PRODUCTS");
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
            case "exit", "0" -> controls.exit();
            case "1", "list" -> list();
            case "2", "find" -> findOne();
//            case "3", "create" -> create();
//            case "4", "update" -> update();
//            case "5", "delete" -> delete();
            default -> p.invalidCommand();
        }
    }

    private void list() {
        p.printH1("ALL PRODUCTS");
        var products = productService.getAllProducts();
        p.printEntities(products);
    }
    private void findOne() {
        p.printH1("FIND PRODUCT");
    }

}
