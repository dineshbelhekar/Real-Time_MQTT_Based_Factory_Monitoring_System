package com.example.IM.PT.controller;

import com.example.IM.PT.DataCache.MqttSubscriber;
import com.example.IM.PT.Entity.DepartmentData;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Entity.PlantData;
import com.example.IM.PT.Entity.User;
import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.service.DataAccessService;
import com.example.IM.PT.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/plant")
public class PlantManagerController {

    @Autowired
    private MqttSubscriber mqttSubscriber;

    @Autowired
    private DataAccessService dataAccessService;

    @Autowired
    private UserService userService;


    @GetMapping("/getallemployee")
    public List<User> getAllUsers() {
        log.info("Employee data accessd by plant manager");
        return userService.getAllUsers();
    }

    @GetMapping("/getLiveData")
    public Map<String, MachineResponse> getLiveData() {
        log.info("live data accessd by plant manager");
        return mqttSubscriber.getLiveData();
    }

    @GetMapping("/plantData")
    public PlantData getPlantData()  {
        log.info("plant data accessd by plant manager");
        return dataAccessService.getAllPlantData();
    }

    @GetMapping("/departmentData")
    public List<DepartmentData> getDepartmentData()  {
        log.info("department data accessd by plant manager");
       return dataAccessService.getAllDepartmentData();
    }

    @GetMapping("/machineData")
    public List<MachineData> getMachineData()  {
        log.info("machine data accessd by plant manager");
        return dataAccessService.getAllMachineData();
    }




}
