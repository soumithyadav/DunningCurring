package com.prodapt.DunningCurring.DTO;

public class CustomerRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String serviceType;
    private String billingType;
    private String serviceIdentifier;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public String getBillingType() { return billingType; }
    public void setBillingType(String billingType) { this.billingType = billingType; }
    public String getServiceIdentifier() { return serviceIdentifier; }
    public void setServiceIdentifier(String serviceIdentifier) { this.serviceIdentifier = serviceIdentifier; }
}