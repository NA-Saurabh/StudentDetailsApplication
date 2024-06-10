package com.example.StudentDetails.Model;


public class ResponseMessage {
	
	private String message;
	private String error;
	
	public ResponseMessage(String message) {
		this.message = message;
	}
	
	public ResponseMessage(String message, String error) {
		this.message = message;
		this.error =error;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}