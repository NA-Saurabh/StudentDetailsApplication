package com.example.StudentDetails.auth;

import java.util.Set;

public interface TokenBlacklist {
	
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
