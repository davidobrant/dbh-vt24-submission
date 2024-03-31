package org.example.init;

import org.example.repositories.BaseRepository;

import java.sql.SQLException;

public class Setup extends BaseRepository {

    public Setup() {
        super();
//        runScripts(createFunctionsScripts);
        runScripts(createTablesScripts);
    }

    private void runScripts(String[] scripts) {
        try {
            var stmt = conn.createStatement();
            for (String script : scripts) {
                stmt.addBatch(script);
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            p.printError("ERROR: " + e.getMessage());
        }
    }

    private final String[] createTablesScripts = new String[]{
            "CREATE TABLE IF NOT EXISTS employees (" +
                    "employee_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "employee_no VARCHAR(45) NULL," +
                    "employee_name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(45) NOT NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "customer_no VARCHAR(45) NULL," +
                    "customer_name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL, " +
                    "phone VARCHAR(45) NOT NULL, " +
                    "address VARCHAR(255) NOT NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "brand VARCHAR(45) NOT NULL," +
                    "model VARCHAR(45) NOT NULL," +
                    "production_year INT," +
                    "customer_id INT NULL, " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");",
            "CREATE TABLE IF NOT EXISTS order_statuses (" +
                    "order_status_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "order_status VARCHAR(45) NOT NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS order_types (" +
                    "order_type_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "order_type VARCHAR(45) NOT NULL," +
                    "price DECIMAL(10,2) DEFAULT 0" +
                    ");",
            "CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "order_no VARCHAR(25) NULL," +
                    "employee_id INT," +
                    "customer_id INT," +
                    "product_id INT NULL," +
                    "order_status_id INT DEFAULT 1," +
                    "order_type_id INT," +
                    "description TEXT," +
                    "notes TEXT," +
                    "has_attachments TINYINT(1) DEFAULT 0, " +
                    "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE SET NULL ON UPDATE CASCADE," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "FOREIGN KEY (order_status_id) REFERENCES order_statuses(order_status_id) ON DELETE SET NULL ON UPDATE CASCADE," +
                    "FOREIGN KEY (order_type_id) REFERENCES order_types(order_type_id) ON DELETE SET NULL ON UPDATE CASCADE" +
                    ");",
            "CREATE TABLE IF NOT EXISTS order_images (" +
                    "order_image_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id INT, " +
                    "image_key VARCHAR(255), " +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE SET NULL ON UPDATE CASCADE" +
                    ");",
            "CREATE TABLE IF NOT EXISTS invoice_statuses (" +
                    "invoice_status_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "invoice_status VARCHAR(45) NOT NULL" +
                    ");",
            "CREATE TABLE IF NOT EXISTS invoices (" +
                    "invoice_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "invoice_no VARCHAR(25) NULL," +
                    "order_no VARCHAR(45)," +
                    "customer_id INT," +
                    "invoice_status_id INT DEFAULT 1," +
                    "amount DECIMAL(10,2)," +
                    "invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "payment_date TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 45 DAY), " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET NULL ON UPDATE CASCADE," +
                    "FOREIGN KEY (invoice_status_id) REFERENCES invoice_statuses(invoice_status_id) ON DELETE SET NULL ON UPDATE CASCADE" +
                    ");"
    };


    /**
    * Only Locally Called - SUPER Privileges needed to run MySQL functions
     * SET log_bin_trust_functions_creator = 1
    * */
    private String[] createFunctionsScripts = new String[]{
            "CREATE FUNCTION IF NOT EXISTS generateCustomerNo() RETURNS INT DETERMINISTIC " +
                    "BEGIN " +
                    "   DECLARE random_num INT; " +
                    "   REPEAT " +
                    "       SET random_num = FLOOR(RAND() * 1000) + 111000000; " +
                    "   UNTIL NOT EXISTS(SELECT * FROM customers WHERE customer_no = random_num) END REPEAT; " +
                    "   RETURN random_num ; " +
                    "END",
            "CREATE FUNCTION IF NOT EXISTS generateEmployeeNo() RETURNS INT DETERMINISTIC " +
                    "BEGIN " +
                    "   DECLARE random_num INT; " +
                    "   REPEAT " +
                    "       SET random_num = FLOOR(RAND() * 1000) + 333000000; " +
                    "   UNTIL NOT EXISTS(SELECT * FROM employees WHERE employee_no = random_num) END REPEAT; " +
                    "   RETURN random_num ; " +
                    "END",
            "CREATE FUNCTION IF NOT EXISTS generateOrderNo() RETURNS INT DETERMINISTIC " +
                    "BEGIN " +
                    "   DECLARE random_num INT; " +
                    "   REPEAT " +
                    "       SET random_num = FLOOR(RAND() * 1000) + 777000000; " +
                    "   UNTIL NOT EXISTS(SELECT * FROM orders WHERE order_no = random_num) END REPEAT; " +
                    "   RETURN random_num ; " +
                    "END",
            "CREATE FUNCTION IF NOT EXISTS generateInvoiceNo() RETURNS INT DETERMINISTIC " +
                    "BEGIN " +
                    "   DECLARE random_num INT; " +
                    "   REPEAT " +
                    "       SET random_num = FLOOR(RAND() * 1000) + 999000000; " +
                    "   UNTIL NOT EXISTS(SELECT * FROM invoices WHERE invoice_no = random_num) END REPEAT; " +
                    "   RETURN random_num ; " +
                    "END",
    };

}
