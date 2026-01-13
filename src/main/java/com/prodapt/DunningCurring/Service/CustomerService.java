package com.prodapt.DunningCurring.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodapt.DunningCurring.DAO.CustomerRepository;
import com.prodapt.DunningCurring.DAO.TelecomServiceRepository;
import com.prodapt.DunningCurring.DAO.UserRepository;
import com.prodapt.DunningCurring.DTO.CustomerRegistrationDTO;
import com.prodapt.DunningCurring.Entity.Customer;
import com.prodapt.DunningCurring.Entity.TelecomService;
import com.prodapt.DunningCurring.Entity.User;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TelecomServiceRepository serviceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerService() {
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer getByCustomerCode(String code) {
        return customerRepository.findByCustomerCode(code);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer registerNewCustomer(CustomerRegistrationDTO dto) {

        //  Validation checks
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        //  Create User 
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); 
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole("CUSTOMER"); 
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        //  Create Customer 
        Customer customer = new Customer();
        customer.setName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        
        // Set the Email in Customer table too
        customer.setEmail(dto.getEmail()); 

        String randomCode = "CUST-" + (1000 + new java.util.Random().nextInt(9000));
        customer.setCustomerCode(randomCode);
        
        customer.setUser(savedUser);
        Customer savedCustomer = customerRepository.save(customer);

        // Create Initial Service
        TelecomService service = new TelecomService();
        service.setCustomer(savedCustomer);
        service.setServiceType(dto.getServiceType());
        service.setIdentifier(dto.getServiceIdentifier());
        service.setBillingType(dto.getBillingType());
        service.setStatus("ACTIVE");
        service.setCurrentOverdueAmount(BigDecimal.ZERO);
        service.setCreatedAt(LocalDateTime.now());
        serviceRepository.save(service);

        return savedCustomer;
    }
}