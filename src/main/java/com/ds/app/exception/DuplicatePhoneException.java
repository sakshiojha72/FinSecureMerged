package com.ds.app.exception;

import lombok.Data;

@Data
public class DuplicatePhoneException extends Exception {
	
	private String phoneNumber;

	public DuplicatePhoneException(String phoneNumber) {
		super();
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "DuplicatePhoneException [phoneNumber=" + phoneNumber + "]";
	}
	
	
	
}
