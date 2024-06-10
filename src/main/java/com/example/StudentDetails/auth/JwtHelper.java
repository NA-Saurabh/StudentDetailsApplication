package com.example.StudentDetails.auth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.StudentDetails.Model.User;

import java.util.Date;
import java.util.List;

@Component
public class JwtHelper {
	
	@Autowired
	TokenBlacklist tokenblacklist;

    private final String secret_key = "CFBRH489349893H5GFUHUHG45HG8H5448HGRUHYF";
    private long accessTokenValidity = 5 * 60 * 1000; 

    private final JwtParser jwtParser = Jwts.parser().setSigningKey(secret_key);

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    
    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + accessTokenValidity);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (tokenblacklist.isBlacklisted(token)) {
            	throw new ExpiredJwtException(jwtParser.parse(token).getHeader(), parseJwtClaims(token), "Token Expired");
            }
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
       
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
        	Date expiration = claims.getExpiration();
        	if (expiration != null) {
                return expiration.after(new Date());
            } else {
                return false;
            }
          
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
