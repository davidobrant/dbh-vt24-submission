package org.example.services;

import org.example.models.Customer;
import org.example.repositories.CustomerRepository;

import java.util.List;

public class CustomerService {

    CustomerRepository customerRepository = new CustomerRepository();


    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer getCustomerById(Integer customerId) {
        return customerRepository.getById(customerId);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.getByEmail(email);
    }

    public Customer addCustomer(Customer newCustomer) {
        return customerRepository.add(newCustomer);
    }

    public Customer updateCustomer(Customer newCustomer) {
        return customerRepository.update(newCustomer);
    }

    public boolean deleteCustomerById(Integer customerId) {
        return customerRepository.delete(customerId);
    }

}
