package com.example.IM.PT.service;

import com.example.IM.PT.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceIMPL implements UserDetailsService {

    @Autowired
     private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       User user = userService.findByUsername(userName);

       if (user != null)
       {
           return org.springframework.security.core.userdetails.User.builder()
                   .username(user.getUsername())
                   .password(user.getPassword())
                   .roles(user.getDesignation())
                   .build();
       }
       throw new UsernameNotFoundException("User not found for Username" + userName);
    }
}
