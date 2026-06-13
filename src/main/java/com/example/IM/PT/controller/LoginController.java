package com.example.IM.PT.controller;

import com.example.IM.PT.request.LoginRequestDTO;
import com.example.IM.PT.service.UserDetailsServiceIMPL;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceIMPL userdetails;

    @PostMapping
    public void login(@RequestBody LoginRequestDTO loginRequestDTO){

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),loginRequestDTO.getPassword()));
            UserDetails userDetails = userdetails.loadUserByUsername(loginRequestDTO.getUsername());
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);

        }
    }


}
