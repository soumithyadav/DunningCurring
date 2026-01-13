package com.prodapt.DunningCurring.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;  
    private String email;
    private String phone;
    private String status = "ACTIVE";

    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToOne(mappedBy = "user")
    @JsonIgnore 
    private Customer customer;
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User(String username, String password, String role, String email, String phone, String status,
			LocalDateTime createdAt) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.createdAt = createdAt;
	}

	public User(Long id, String username, String password, String role, String email, String phone, String status,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.status = status;
		this.createdAt = createdAt;
	}

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", email="
				+ email + ", phone=" + phone + ", status=" + status + ", createdAt=" + createdAt + "]";
	}

	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

}

