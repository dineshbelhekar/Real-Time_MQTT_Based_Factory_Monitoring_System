package com.example.IM.PT.controller;

import com.example.IM.PT.Entity.User;
import com.example.IM.PT.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/employee")
public class AdminController {

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    public User createUser(@RequestBody User user) {
        userService.saveNewUser(user);
        return user;
    }

    @GetMapping("/getall")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }


    @DeleteMapping("/delete/{Employeeid}")
    public void deleteUser(@PathVariable String Employeeid) {
        userService.deleteUser(Employeeid);
    }

    @GetMapping("/getbyid/{employeeId}")
    public ResponseEntity<User> getByEmployeeID(@PathVariable String employeeId) {
        return ResponseEntity.ok(
                userService.getByEmployeeID(employeeId));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
