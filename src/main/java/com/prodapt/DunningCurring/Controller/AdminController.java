package com.prodapt.DunningCurring.Controller;

import com.prodapt.DunningCurring.DTO.CustomerRegistrationDTO;
import com.prodapt.DunningCurring.Entity.Customer;
import com.prodapt.DunningCurring.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private CustomerService customerService;

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