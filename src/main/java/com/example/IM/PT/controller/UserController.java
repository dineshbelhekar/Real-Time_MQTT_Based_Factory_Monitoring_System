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

    @Autowired
    private MqttSubscriber mqttSubscriber;

    private DataSavingScheduler dataSavingScheduler;




//    @GetMapping
//    public List<User> getAllUsers()
//    {
//        return userService.getAllUsers();
//    }

    @GetMapping//("/livedata")
    public Map<String, MachineResponse> livedata()
    {

        return mqttSubscriber.getLiveData();

    }

    @PostMapping
    public User createUser(@RequestBody User user)
    {
        userService.saveNewUser(user);
        return user;
    }

}
