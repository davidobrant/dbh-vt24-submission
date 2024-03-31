package org.example.ui.menus.admin;

import org.example.forms.EmployeeForm;
import org.example.models.Employee;
import org.example.services.EmployeeService;
import org.example.supers.Controls;
import org.example.utils.Printer;

import java.util.NoSuchElementException;

public class HandleEmployees {

    Controls controls;
    Printer p = new Printer();
    private final EmployeeService employeeService = new EmployeeService();

    public void run() {
        controls = new Controls();
        while (controls.isRunning()) {
            menu();
        }
    }

    private void menu() {
        p.menu("EMPLOYEES");
        System.out.print("""
        [1] List
        [2] Find One
        [3] Add new
        [4] Update
        [5] Delete
        [0] Exit
        """);
        handleMenu(p.promptCommand());
    }

    private void handleMenu(String cmd) {
        switch (cmd) {
            case "exit", "0" -> controls.exit();
            case "1", "list" -> list();
            case "2", "find" -> findOne();
            case "3", "create" -> create();
            case "4", "update" -> update();
            case "5", "delete" -> delete();
            default -> p.invalidCommand();
        }
    }

    private void list() {
        p.printH1("ALL EMPLOYEES");
        var employees = employeeService.getAllEmployees();
        p.printEmployees(employees);
    }
    private void findOne() {
        p.printH1("FIND EMPLOYEE");
        var employee = promptEmployeeById();
        p.printEmployee(employee);
    }
    private void create() {
        p.printH1("CREATE EMPLOYEE");
        var newEmployee = new EmployeeForm().create();
        var employee = employeeService.addEmployee(newEmployee);
        p.printEmployee(employee);
    }
    private void update() {
        p.printH1("UPDATE EMPLOYEE");
        var employee = promptEmployeeById();
        if (employee == null) return;
        var updatedEmployee = new EmployeeForm().update(employee);
        p.printEmployee(updatedEmployee);
    }
    private void delete() {
        p.printH1("DELETE EMPLOYEE");
        var employee = promptEmployeeById();
        if (employee == null) return;
        var employeeId = employee.getEmployeeId();
        if (p.promptContinue("DELETE Employee with ID: " + employeeId)) {
            employeeService.deleteEmployeeById(employeeId);
        }
    }

    private Employee promptEmployeeById() {
        try {
            var employeeId = p.promptInt("Employee ID");
            if (employeeId == 0) return null;
            return employeeService.getEmployeeById(employeeId);
        } catch (NoSuchElementException e) {
            p.noResultsFound();
            return null;
        }
    }
}
