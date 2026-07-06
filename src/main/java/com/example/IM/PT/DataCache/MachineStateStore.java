package com.example.IM.PT.DataCache;

import com.example.IM.PT.enums.MachineAlertStatus;
import com.example.IM.PT.service.RedisService;
import org.springframework.stereotype.Component;


@Component
public class MachineStateStore {

    private final RedisService redisService;


    public MachineStateStore(RedisService redisService) {
        this.redisService = redisService;
    }


    public boolean isAlertAssigned(String machineId) {
        if (machineId == null) {
            return false;
        }
        MachineAlertStatus alertStatus = redisService.getMachineStatus(machineId);
        return MachineAlertStatus.ASSIGNED.equals(alertStatus);
    }

    public boolean isAlertRaised(String machineID){
        if (machineID == null) {
            return false;
        }
        MachineAlertStatus alertStatus = redisService.getMachineStatus(machineID);
        return MachineAlertStatus.ALERT_RAISED.equals(alertStatus);

    }

    public void setStatus(String machineId, MachineAlertStatus status){
        redisService.saveMachineStatus(machineId,status);
    }

    public void clearAlert(String machineId) {

        redisService.removeMachineStatus(machineId);
    }

}
