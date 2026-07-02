package com.example.IM.PT.controller;


import com.example.IM.PT.Entity.MaintenanceAlert;
import com.example.IM.PT.Entity.User;
import com.example.IM.PT.service.MaintenanceAlertService;
import com.example.IM.PT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/general")
public class GeneralController {

    @Autowired
    private UserService userService;

    @Autowired
    private MaintenanceAlertService maintenanceAlertService;

    @GetMapping("/role")
    public String getRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return user.getDesignation();
    }

    @PostMapping("/maintenance_alert/accept")
    public ResponseEntity<String> alertAccept(@RequestBody Map<String, String> body) {
        try {
            String machineId = body.get("machineID");
            String str = maintenanceAlertService.assignMaintenance(machineId);
            return new ResponseEntity<>(str, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Maintenance is not Assigned", HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/maintenance_alert/completed")
    public ResponseEntity<String> maintenanceCompleted(@RequestBody Map<String, String> body){
        String machineId = body.get("machineID");
        String str = maintenanceAlertService.completeMaintenance(machineId);
       return new ResponseEntity<>("Maintainance Done " + machineId,HttpStatus.OK);
    }


}
