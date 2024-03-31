package org.example.forms;

import org.example.models.Employee;
import org.example.services.EmployeeService;
import org.example.supers.Controls;
import org.example.utils.Formatter;
import org.example.utils.Printer;

public class EmployeeForm {

    EmployeeService employeeService = new EmployeeService();
    Printer p = new Printer();
    Formatter f = new Formatter();

    public Employee create() {
        var employeeName = p.prompt("NAME");
        var email = promptEmail("EMAIL");
        var phone = p.prompt("PHONE");
        return new Employee(employeeName, email, phone);
    }

    public Employee update(Employee employee) {
        Controls controls = new Controls();
        while (controls.isRunning()) {
            System.out.print("\n" +
                    "[1] Name " + f.parentheses(employee.getEmployeeName()) + "\n" +
                    "[2] Email " +  f.parentheses(employee.getEmail()) + "\n" +
                    "[3] Phone " + f.parentheses(employee.getPhone()) + "\n" +
                    "[0] Exit "
            );
            String cmd = p.promptCommand();
            switch (cmd) {
                case "exit", "0" -> {
                    controls.exit();
                }
                case "1", "name" -> {
                    var employeeName = p.prompt("NAME");
                    employee.setEmployeeName(employeeName);
                    employeeService.updateEmployee(employee);
                }
                case "2", "email" -> {
                    var email = promptEmail("EMAIL");
                    employee.setEmail(email);
                    employeeService.updateEmployee(employee);
                }
                case "3", "phone" -> {
                    var phone = p.prompt("PHONE");
                    employee.setPhone(phone);
                    employeeService.updateEmployee(employee);
                }
                default -> p.invalidCommand();
            }
        }
        return employee;
    }

    private String promptEmail(String value) {
        try {
            var email = p.prompt(value);
            if (employeeService.getEmployeeByEmail(email) != null) {
                throw new Exception("Email already exists.");
            }
            return email;
        } catch (Exception e) {
            p.printError(e.getMessage());
            return promptEmail(value);
        }
    }


}
