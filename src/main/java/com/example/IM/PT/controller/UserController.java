package com.example.IM.PT.controller;

import com.example.IM.PT.DataCache.MqttSubscriber;
import com.example.IM.PT.Entity.User;
import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.scheduler.DataSavingScheduler;
import com.example.IM.PT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;



//    @GetMapping
//    public List<User> getAllUsers()
//    {
//        return userService.getAllUsers();
//    }


    @PostMapping
    public User createUser(@RequestBody User user)
    {
        userService.saveNewUser(user);
        return user;
    }

    @DeleteMapping("/{Employeeid}")
    public void deleteUser(@PathVariable String Employeeid)
    {
        userService.deleteUser(Employeeid);
    }

}
