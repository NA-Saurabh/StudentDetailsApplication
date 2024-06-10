package com.example.StudentDetails.Model;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
	HttpStatus httpStatus;
    String message;
    String error;

    public ErrorResponse(HttpStatus httpStatus, String message, String error) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.error = error;
    }
    
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
