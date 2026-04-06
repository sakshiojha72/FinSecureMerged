package com.ds.app.exception;

import lombok.Data;

@Data
public class ProfileDeletedException extends Exception {
	
	private Long userId;

	public ProfileDeletedException(Long userId) {
		super();
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ProfileDeletedException [userId=" + userId + "]";
	}
	
	

}
