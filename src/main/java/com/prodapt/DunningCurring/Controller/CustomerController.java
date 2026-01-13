package com.prodapt.DunningCurring.Controller;

import java.security.Principal;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.prodapt.DunningCurring.DAO.CustomerRepository;
import com.prodapt.DunningCurring.DAO.UserRepository;
import com.prodapt.DunningCurring.Entity.Customer;
import com.prodapt.DunningCurring.Entity.User;
import com.prodapt.DunningCurring.Service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerRepository customerRepository; // Ensure this is injected
    
 
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/customer/my-profile")
    public Customer getMyProfile(Principal principal) {
//        System.out.println("Controller is working fine");

        String username = principal.getName();
//        System.out.println("Username: " + username);

        User user = userRepository.findByUsername(username).orElseThrow();
//        System.out.println("Email : " + user.getEmail());

        Customer customer = customerRepository.findByEmail(user.getEmail());
//        System.out.println("Checking customer details: " + customer);

        return customer;
    }

    @GetMapping("/admin/customers")
    public Iterable<Customer> getAllCustomers1() {
        return customerRepository.findAll();
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }
    @GetMapping("/me")
    public Customer getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
 
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return customerRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Customer profile not found for this user"));
    }
}

