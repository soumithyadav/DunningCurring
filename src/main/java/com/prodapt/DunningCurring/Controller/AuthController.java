package com.prodapt.DunningCurring.Controller;

import com.prodapt.DunningCurring.DTO.CustomerRegistrationDTO;
import com.prodapt.DunningCurring.Entity.Customer;
import com.prodapt.DunningCurring.Security.JwtUtil;
import com.prodapt.DunningCurring.Service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    private CustomerService customerService;

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody AuthRequest authRequest) {

		// Authenticate the user credentials
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		// Load User Details
		UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

		// Extract Role to give access to both admin and customer differently
		String role = "CUSTOMER"; 
		if (!userDetails.getAuthorities().isEmpty()) {
			role = userDetails.getAuthorities().iterator().next().getAuthority();
		}

		// Generate Token with jWTUtil 
		String token = jwtUtil.generateToken(userDetails.getUsername(), role);

		//  Return JSON Response
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		response.put("role", role);

		return response;
	}
	@PostMapping("/create-customer")
    public String createCustomer(@RequestBody CustomerRegistrationDTO dto) {
        try {
            Customer newCustomer = customerService.registerNewCustomer(dto);
            return "Customer created successfully with ID: " + newCustomer.getId();
        } catch (Exception e) {
            throw new RuntimeException("Creation failed: " + e.getMessage());
        }
    }
}

class AuthRequest {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}