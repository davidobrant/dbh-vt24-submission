package org.example.services;

import org.example.models.Customer;
import org.example.models.Employee;
import org.example.repositories.EmployeeRepository;
import org.example.supers.Utils;

import java.util.List;

public class EmployeeService extends Utils {

    EmployeeRepository employeeRepository = new EmployeeRepository();

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAll();
    }

    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.getById(employeeId);
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.getByEmail(email);
    }

    public Employee addEmployee(Employee newEmployee) {
        return employeeRepository.add(newEmployee);
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.update(employee);
    }

    public boolean deleteEmployeeById(Integer employeeId) {
        return employeeRepository.delete(employeeId);
    }

    public Employee chooseEmployeeByListChoice() {
        var employees = getAllEmployees();
        var index = u.printChoosingList(employees);
        if (index == null) return null;
        return employees.get(index);
    }
}
