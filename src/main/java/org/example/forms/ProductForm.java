package org.example.forms;

import org.example.models.Product;
import org.example.utils.Printer;

public class ProductForm {

    Printer p = new Printer();

    public Product create() {
        var brand = p.prompt("BRAND");
        var model = p.prompt("MODEL");
        var productionYear = p.promptInt("PRODUCTION YEAR");
        return new Product(brand, model, productionYear);
    }


}
