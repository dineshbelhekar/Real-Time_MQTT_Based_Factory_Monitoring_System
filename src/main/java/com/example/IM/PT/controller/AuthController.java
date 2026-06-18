package com.example.IM.PT.controller;

import com.example.IM.PT.request.LoginRequestDTO;
import com.example.IM.PT.service.UserDetailsServiceIMPL;
import com.example.IM.PT.service.UserService;
import com.example.IM.PT.util.JWTutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceIMPL userdetails;

    @Autowired
    private JWTutil jwTutil;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO){

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),loginRequestDTO.getPassword()));
            UserDetails userDetails = userdetails.loadUserByUsername(loginRequestDTO.getUsername());
            String jwt = jwTutil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);

        }
    }

}
