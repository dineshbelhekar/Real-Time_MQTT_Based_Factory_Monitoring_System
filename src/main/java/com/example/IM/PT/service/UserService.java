package com.example.IM.PT.service;

import com.example.IM.PT.Entity.User;
import com.example.IM.PT.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;



    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public  User findByUsername(String username)
    {
        try {
            return userRepository.findByUsername(username);
        }
        catch (Exception e) {
            log.error(" User not found for username " + username);
            throw new RuntimeException(" User not found ", e);
        }

    }


    public void saveNewUser(User user)
    {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error Occured for user " + user.getUsername());
            throw new RuntimeException(e+ " User not saved");

        }

    }

    public String getUserDepartment()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user  = findByUsername(username);
        return user.getDepartment();
    }

    public List<User> getAllUsers()
    {
         return userRepository.findAll();
    }
}
