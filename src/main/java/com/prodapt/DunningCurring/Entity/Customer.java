package com.prodapt.DunningCurring.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;        
    private String lastName;    


    @Column(unique = true)
    private String customerCode;
    
    public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference 
    private List<TelecomService> services;

    public List<TelecomService> getServices() {
        return services;
    }

    public void setServices(List<TelecomService> services) {
        this.services = services;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Customer(Long id, User user, String name, String customerCode, LocalDateTime createdAt, String email) {
		super();
		this.id = id;
		this.user = user;
		this.name = name;
		this.customerCode = customerCode;
		this.createdAt = createdAt;
		this.email=email;
	}

	public Customer(User user, String name, String customerCode, LocalDateTime createdAt, String email) {
		super();
		this.user = user;
		this.name = name;
		this.customerCode = customerCode;
		this.createdAt = createdAt;
		this.email=email;
	}

	public Customer() {
		super();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", user=" + user + ", name=" + name + ", customerCode=" + customerCode
				+ ", createdAt=" + createdAt +"email"+ email+ "]";
	}
}
