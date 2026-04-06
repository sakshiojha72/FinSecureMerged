package com.ds.app.exception;

import lombok.Data;

@Data
public class AccountLockedException extends Exception {
	
	Long userId;

	public AccountLockedException(Long userId) {
		super();
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AccountLockedException [userId=" + userId + "]";
	}
	
	

}
