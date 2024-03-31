package org.example.repositories;

import org.example.models.Customer;
import org.example.models.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends BaseRepository {

    public ProductRepository() {
        super();
    }

    public List<Product> getAll() {
        return findAll(Product.class);
    }

    public List<Product> getAllByCustomerId(Integer customerId) {
        return findAllByForeignKey(Product.class, Customer.class, customerId);
    }

    public List<Product> getProductsNotInActiveOrderByCustomer(Integer customerId) {
        try {
            List<Product> list = new ArrayList<>();
            String sql = "SELECT p.* FROM products p WHERE customer_id = ? " +
                "AND NOT EXISTS (" +
                "SELECT 1 FROM orders o WHERE o.product_id = p.product_id " +
                "AND o.order_status_id != 'closed');";

            var pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);

            var rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(createEntityFromRS(rs, Product.class));
            }

            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Product add(Product product) {
        return create(Product.class, product);
    }

    public boolean deleteById(Integer productId) {
        return deleteById(Product.class, productId);
    }

    public boolean deleteByIdAuthCustomerId(Integer productId, Integer customerId) {
        return deleteByIdAuthId(Product.class, productId, Customer.class, customerId);
    }

    public Integer getCountByCustomerId(Integer customerId) {
        return getCountByAuthId(Product.class, Customer.class, customerId);
    }
}
