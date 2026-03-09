package com.cinema.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cinema.enums.EmployeeGender;
import com.cinema.enums.EmployeeStatus;

public class Employee {
	private int employeeId;
	private String employeeName;
	private EmployeeGender employeeGender;
	private String employeePhoneNumber;
	private String employeeEmail;
	private LocalDate hireDate;
	private JobTitle jobTitleId;
	private EmployeeStatus employeeStatus;
	private LocalDateTime createdAt;
	private LocalDateTime UpdatedAt;
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}
//TODO: theme gettersettertostring
}
