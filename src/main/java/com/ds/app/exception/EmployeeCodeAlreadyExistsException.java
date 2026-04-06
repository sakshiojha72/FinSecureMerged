package com.ds.app.exception;

import lombok.Data;

@Data
public class EmployeeCodeAlreadyExistsException extends Exception {
	
	private String employeeCode;

	public EmployeeCodeAlreadyExistsException(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "EmployeeCodeAlreadyExistsException [employeeCode=" + employeeCode + "]";
	}
}
