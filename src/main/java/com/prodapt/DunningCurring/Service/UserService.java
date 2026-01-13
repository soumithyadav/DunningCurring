
package com.prodapt.DunningCurring.Service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.DunningCurring.DAO.UserRepository;
import com.prodapt.DunningCurring.Entity.User;

@Service
public class UserService {

	 @Autowired
    private final UserRepository userRepository;

   
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
