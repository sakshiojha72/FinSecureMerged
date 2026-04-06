package com.ds.app.exception;

import lombok.Data;

@Data
public class EmployeeNotFoundException extends Exception {
	
	private String username;
	private Long userId;

	public EmployeeNotFoundException(String username) {
		super();
		this.username = username;
	}
	
	
	public EmployeeNotFoundException(Long userId) {
		super();
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "EmployeeNotFoundException [username=" + username + ", userId=" + userId + "]";
	}



	

}
