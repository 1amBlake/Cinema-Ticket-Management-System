package com.cinema.service;

import java.sql.SQLException;
import java.util.List;

import com.cinema.dao.EmployeeDao;
import com.cinema.entity.Employee;


public class EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeService() {
        this.employeeDao = new EmployeeDao();
    }


    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDao.getAllEmployees();
    }


    public Employee findEmployeeById(int id) throws SQLException {
        return employeeDao.findById(id);
    }


    public boolean addEmployee(Employee employee) throws SQLException {
        validate(employee);
        return employeeDao.addEmployee(employee);
    }


    public boolean updateEmployee(Employee employee) throws SQLException {
        validate(employee);
        return employeeDao.updateEmployee(employee);
    }


    public boolean deleteEmployee(int id) throws SQLException {
        return employeeDao.deleteEmployeeById(id);
    }


    public List<Employee> searchEmployees(String keyword) throws SQLException {
        return employeeDao.searchByName(keyword);
    }


    private void validate(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee is null");
        }

        if (employee.getEmployeeName() == null || employee.getEmployeeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name is required");
        }

        if (employee.getJobTitle() == null) {
            throw new IllegalArgumentException("Job title is required");
        }

        if (employee.getEmployeeStatus() == null) {
            throw new IllegalArgumentException("Employee status is required");
        }
    }
}