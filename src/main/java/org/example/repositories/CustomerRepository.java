package org.example.repositories;

import org.example.models.Customer;

import java.sql.Connection;
import java.util.List;

public class CustomerRepository extends BaseRepository {

    public CustomerRepository() {
        super();
    }

    public List<Customer> getAll() {
        return findAll(Customer.class);
    }

    public Customer getById(Integer customerId) {
        return findById(Customer.class, customerId);
    }

    public Customer getByEmail(String email) {
        return findByEmail(Customer.class, email);
    }

    public Customer add(Customer customer) { return create(Customer.class, customer); }

    public Customer update(Customer customer) {
        return update(Customer.class, customer);
    }

    public boolean delete(Integer customerId) {
        return deleteById(Customer.class, customerId);
    }


}
