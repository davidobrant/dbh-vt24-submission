package org.example.repositories;

import org.example.models.Employee;

import java.util.List;

public class EmployeeRepository extends BaseRepository {

    public EmployeeRepository() {
        super();
    }

    public List<Employee> getAll() {
        return findAll(Employee.class);
    }

    public Employee getById(Integer employeeId) {
        return findById(Employee.class, employeeId);
    }

    public Employee getByEmail(String email) {
        return findByEmail(Employee.class, email);
    }

    public Employee add(Employee employee) {
        return create(Employee.class, employee);
    }

    public Employee update(Employee employee) {
        return update(Employee.class, employee);
    }

    public boolean delete(Integer employeeId) {
        return deleteById(Employee.class, employeeId);
    }

}
