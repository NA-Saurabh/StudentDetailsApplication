package com.example.StudentDetails.Model;

import jakarta.servlet.http.Cookie;

public class LoginResponse {
	private String email;
    private String token;
    private String refreshToken;
    private Cookie cookies;

    public LoginResponse(String email, String token, Cookie cookies) {
        this.email = email;
        this.token = token;
        this.cookies = cookies;
    }
    
    
	public Cookie getCookies() {
		return cookies;
	}


	public void setCookies(Cookie cookies) {
		this.cookies = cookies;
	}


	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
