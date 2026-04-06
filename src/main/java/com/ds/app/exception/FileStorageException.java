package com.ds.app.exception;

import lombok.Data;

@Data
public class FileStorageException extends Exception {

	    private String message;

	    public FileStorageException(String message) {
	        this.message = message;
	    }
	    
	    
	    
	    @Override
	    public String toString() {
	        return "FileStorageException [message=" + message + "]";
	    }



		public FileStorageException(String message, Throwable cause) {
			super(message, cause);
			
		}
	
}
