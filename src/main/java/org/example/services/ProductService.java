package org.example.services;

import org.example.models.Product;
import org.example.repositories.ProductRepository;

import java.util.List;

public class ProductService {

    ProductRepository productRepository = new ProductRepository();

    public List<Product> getAllProducts() {
        return productRepository.getAll();
    }

    public List<Product> getAllProductsByCustomer(Integer customerId) {
        return productRepository.getAllByCustomerId(customerId);
    }

    public List<Product> getProductsNotInActiveOrderByCustomer(Integer customerId) {
        return productRepository.getProductsNotInActiveOrderByCustomer(customerId);
    }

    public Product addProduct(Product product) {
        return productRepository.add(product);
    }

    public boolean deleteProductById(Integer productId) {
        return productRepository.deleteById(productId);
    }

    public boolean deleteProductByIdAuthCustomerId(Integer productId, Integer customerId) {
        return productRepository.deleteByIdAuthCustomerId(productId, customerId);
    }

    public Integer getProductCountByCustomerId(Integer customerId) {
        return productRepository.getCountByCustomerId(customerId);
    }
}
