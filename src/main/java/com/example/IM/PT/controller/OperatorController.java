package com.example.IM.PT.controller;

import com.example.IM.PT.DataCache.MqttSubscriber;
import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/machine")
public class OperatorController {

    @Autowired
    private UserService userService;

    @Autowired
    private MqttSubscriber mqttSubscriber;

    @GetMapping("/getLiveData")
    public List<MachineResponse> getLiveData()
    {
        String Department = userService.getUserDepartment();
        return mqttSubscriber.getLiveData().values().stream()
                .filter(m -> Department.equals(m.getDepartment()))
                .toList();

    }
}
