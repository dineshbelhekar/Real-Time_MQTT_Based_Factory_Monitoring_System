package com.example.IM.PT.controller;

import com.example.IM.PT.DataCache.MqttSubscriber;
import com.example.IM.PT.Entity.DepartmentData;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Entity.PlantData;
import com.example.IM.PT.Entity.User;
import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.service.DataAccessService;
import com.example.IM.PT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plant")
public class AdminController {

    @Autowired
    private MqttSubscriber mqttSubscriber;

    @Autowired
    private DataAccessService dataAccessService;

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user)
    {
        userService.saveNewUser(user);
        return user;
    }

    @GetMapping("/getLiveData")
    public Map<String, MachineResponse> getLiveData()
    {
        return mqttSubscriber.getLiveData();
    }

    @GetMapping("/plantData")
    public PlantData getPlantData()  {
        return dataAccessService.getAllPlantData();
    }

    @GetMapping("/departmentData")
    public List<DepartmentData> getDepartmentData()  {
       return dataAccessService.getAllDepartmentData();
    }

    @GetMapping("/machineData")
    public List<MachineData> getMachineData()  {
        return dataAccessService.getAllMachineData();
    }




}
