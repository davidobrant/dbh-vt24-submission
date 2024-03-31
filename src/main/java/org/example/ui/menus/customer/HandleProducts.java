package org.example.ui.menus.customer;

import org.example.forms.ProductForm;
import org.example.models.Customer;
import org.example.models.Product;
import org.example.services.ProductService;
import org.example.supers.Login;

import java.util.List;


public class HandleProducts extends Login<Customer> {

    ProductService productService = new ProductService();

    List<Product> products;

    public void run(Customer currentUser) {
        this.currentUser = currentUser;
        super.run();

        while (running) menu();
    }

    private void menu() {
        products = productService.getAllProductsByCustomer(currentUser.getCustomerId());
        p.menu("MY PHONES");
        System.out.print(
            "[1] List (" + products.size() + ")\n" +
            "[2] Add \n" +
            "[3] Remove \n" +
            "[0] Exit\n"
        );
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> exit();
            case "1", "list" -> print();
            case "2", "add" -> add();
            case "3", "remove" -> remove();
            default -> p.invalidCommand();
        }
    }

    private void print() {
        p.printH1("MY PHONE LIST");
        if (products.isEmpty()) {
            p.printMessage("No registered phones... Add one!");
            return;
        }
        p.printEntities(products);
    }

    private void add() {
        var newProduct = new ProductForm().create();
        newProduct.setCustomerId(currentUser.getCustomerId());
        var product = productService.addProduct(newProduct);
        p.printEntity(product);
    }

    private void remove() {
        var index = u.printChoosingList(products);
        productService.deleteProductByIdAuthCustomerId(products.get(index).getProductId(), currentUser.getCustomerId());
    }

}
