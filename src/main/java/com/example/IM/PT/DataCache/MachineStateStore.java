package com.example.IM.PT.DataCache;

import com.example.IM.PT.enums.MachineAlertStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MachineStateStore {

    // true -> Alert already created
    // false -> No active alert
    private final ConcurrentHashMap<String, MachineAlertStatus> alertMap =
            new ConcurrentHashMap<>();

    public boolean isAlertAssigned(String machineId) {
        if (machineId == null) {
            return false;
        }
        return alertMap.getOrDefault(machineId, MachineAlertStatus.NOT_RAISED).equals(MachineAlertStatus.ASSIGNED);
    }

    public boolean isAlertRaised(String machineID){
        if (machineID == null) {
            return false;
        }
        return alertMap.getOrDefault(machineID,MachineAlertStatus.NOT_RAISED).equals(MachineAlertStatus.ALERT_RAISED);
    }

    public void setStatus(String machineId, MachineAlertStatus status){
        alertMap.put(machineId,status);
    }

    public void clearAlert(String machineId) {
        alertMap.remove(machineId);
    }
}
