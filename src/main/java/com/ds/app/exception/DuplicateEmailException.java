package com.ds.app.exception;

import lombok.Data;

@Data
public class DuplicateEmailException extends Exception {
	
	private String email;

	public DuplicateEmailException(String email) {
		super();
		this.email = email;
	}

	@Override
	public String toString() {
		return "DuplicateEmailException [email=" + email + "]";
	}
	
	

}
