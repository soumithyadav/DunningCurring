package com.prodapt.DunningCurring.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodapt.DunningCurring.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

