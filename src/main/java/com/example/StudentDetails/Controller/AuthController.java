package com.example.StudentDetails.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.StudentDetails.Model.ErrorResponse;
import com.example.StudentDetails.Model.LoginRequest;
import com.example.StudentDetails.Model.LoginResponse;
import com.example.StudentDetails.Model.User;
import com.example.StudentDetails.Repository.UserRepository;
import com.example.StudentDetails.auth.JwtHelper;
import com.example.StudentDetails.auth.TokenBlacklist;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired 
	private TokenBlacklist tokenBlacklist;

	@Autowired
	private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/register")
    public ResponseEntity<?> login(@RequestBody User user)  {

        try {
        	logger.info(user.getEmail() + " is trying to register.");
        	
            int result = userRepository.addUser(user);
            
            if(result == 1) {
            	
            	logger.info(user.getEmail() + "registered Successfully.");
                return ResponseEntity.ok().body("User Registered Successfully");
            }else {
            	
            	logger.info("User not registered");
                return ResponseEntity.badRequest().body("Unable to register the user.");
            }

        }catch (Exception e){
            
        	logger.error("Unable to Register: " + user.getEmail() + " :" + e.getMessage());
        	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Unable to Register" ,e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq, HttpServletRequest request, HttpServletResponse response)  {

        try {
        	logger.info(loginReq.getEmail() + " trying to Login.");
        	
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email,"");
            String token = jwtUtil.createToken(user);
           
            HttpSession session = request.getSession();
            session.setAttribute("username", loginReq.getEmail());
            
            Cookie cookie = new Cookie("username",loginReq.getEmail());
            cookie.setMaxAge(300);
            response.addCookie(cookie);
            
            LoginResponse loginRes = new LoginResponse(email,token,cookie);
            logger.info(loginReq.getEmail() + "Logged in Successfully.");
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(loginRes);

        }catch (BadCredentialsException e){
        	
        	logger.warn("Incorrect Credentials provided by " + loginReq.getEmail());
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,"Invalid username or password",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            
        	logger.error("Unable to login: " + loginReq.getEmail() + " :" + e.getMessage());
        	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Unable to login" ,e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        
    	String user = request.getCookies()[0].getName();
    	String token = jwtUtil.resolveToken(request);
    	
    	if (token != null && token.length() > 0) {
    		  		
            tokenBlacklist.addToBlacklist(token);
            
            HttpSession session = request.getSession(false); 
            if (session != null) {
                session.invalidate();
            }
            
            Cookie cookie = new Cookie("username",null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            
            logger.info(user + " logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        } else {
        	
        	logger.info("No user logged in.");
            return new ResponseEntity<>("No User Found", HttpStatus.NOT_FOUND);
        }
    }
}
