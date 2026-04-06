package com.ds.app.exception;

import lombok.Data;

@Data
public class ProfileAlreadyExistsException extends Exception{
	
	Long userId;

	public ProfileAlreadyExistsException(Long userId) {
		super();
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ProfileAlreadyExistsException [userId=" + userId + "]";
	}
	
	

}
