package com.example.IM.PT.DataCache;

import com.example.IM.PT.enums.MachineAlertStatus;
import com.example.IM.PT.service.RedisService;
import org.springframework.stereotype.Component;


@Component
public class MachineStateStore {

    private final RedisService redisService;

    // true -> Alert already created
    // false -> No active alert
//    private final ConcurrentHashMap<String, MachineAlertStatus> alertMap =
//            new ConcurrentHashMap<>();

    public MachineStateStore(RedisService redisService) {
        this.redisService = redisService;
    }


    public boolean isAlertAssigned(String machineId) {
        if (machineId == null) {
            return false;
        }
       //alertMap.getOrDefault(machineId, MachineAlertStatus.NOT_RAISED).equals(MachineAlertStatus.ASSIGNED);
        MachineAlertStatus alertStatus = redisService.getMachineStatus(machineId);
        return MachineAlertStatus.ASSIGNED.equals(alertStatus);
    }

    public boolean isAlertRaised(String machineID){
        if (machineID == null) {
            return false;
        }
        //alertMap.getOrDefault(machineID,MachineAlertStatus.NOT_RAISED).equals(MachineAlertStatus.ALERT_RAISED);
        MachineAlertStatus alertStatus = redisService.getMachineStatus(machineID);
        return MachineAlertStatus.ALERT_RAISED.equals(alertStatus);

    }

    public void setStatus(String machineId, MachineAlertStatus status){
       //alertMap.put(machineId,status);
        redisService.saveMachineStatus(machineId,status);
    }

    public void clearAlert(String machineId) {
        //alertMap.remove(machineId);
        System.out.println("called clear alert");
        redisService.removeMachineStatus(machineId);
        System.out.println("completed clear alert");
    }
}
