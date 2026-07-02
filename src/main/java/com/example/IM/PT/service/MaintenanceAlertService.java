package com.example.IM.PT.service;

import com.example.IM.PT.DTO.MaintenanceAlertDTO;
import com.example.IM.PT.DataCache.ConnectedUsersStore;
import com.example.IM.PT.DataCache.MachineStateStore;
import com.example.IM.PT.Entity.MaintenanceAlert;
import com.example.IM.PT.enums.MachineAlertStatus;
import com.example.IM.PT.enums.MaintenanceStatus;
import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.repository.MaintenanceAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MaintenanceAlertService {

    private final MachineStateStore machineStateStore;
    private final MaintenanceAlertRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConnectedUsersStore connectedUsersStore;

    public MaintenanceAlertService(
            MachineStateStore machineStateStore,
            MaintenanceAlertRepository repository,
            SimpMessagingTemplate messagingTemplate,
            ConnectedUsersStore connectedUsersStore) {

        this.machineStateStore = machineStateStore;
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
        this.connectedUsersStore = connectedUsersStore;
    }

    public void handleMachineFailure(MachineResponse machine) {

        if (machineStateStore.isAlertAssigned(machine.getMachineId())) {
            return;
        }

        MaintenanceAlertDTO alert = new MaintenanceAlertDTO();

        alert.setMachineId(machine.getMachineId());
        alert.setDepartment(machine.getDepartment());
        alert.setFailTime(LocalDateTime.now());

        alert.setMessage("Machine is failed need Maintenance ");
        connectedUsersStore.getAllTechnicians().forEach((username,available) -> {
            if (Boolean.TRUE.equals(available)){
                messagingTemplate.convertAndSendToUser(
                        username,
                        "/queue/messages",
                        alert);
            }
        });

        if (!machineStateStore.isAlertRaised(machine.getMachineId())) {
            MaintenanceAlert maintenanceAlert = new MaintenanceAlert();

            maintenanceAlert.setMachineID(machine.getMachineId());
            maintenanceAlert.setDepartment(machine.getDepartment());
            maintenanceAlert.setFailTime(LocalDateTime.now());
            maintenanceAlert.setMaintenanceStatus(MaintenanceStatus.PENDING);
            repository.save(maintenanceAlert);
            machineStateStore.setStatus(machine.getMachineId(), MachineAlertStatus.ALERT_RAISED);
        }

    }


    public void handleMachineRecovery(MachineResponse machine) {

        if (!machineStateStore.isAlertAssigned(machine.getMachineId())) {
            return;
        }

        machineStateStore.clearAlert(machine.getMachineId());
        MaintenanceAlert maintenanceAlert = getAlertData(machine.getMachineId());
        if (maintenanceAlert != null){
        maintenanceAlert.setMaintenanceStatus(MaintenanceStatus.DONE);
        maintenanceAlert.setRecoverTime(LocalDateTime.now());
        maintenanceAlert.setDowntime(Duration.between(maintenanceAlert.getFailTime(),LocalDateTime.now()));
        repository.save(maintenanceAlert);
        connectedUsersStore.resetTechnicianStatus(maintenanceAlert.getTechnician());
        } else {
            log.error("Maintenance Alert is not updated ");
        }
    }

    public MaintenanceAlert getAlertData(String machineID){
        try {
            return repository.findTopByMachineIDOrderByFailTimeDesc(machineID);
        } catch (Exception e) {
            log.info(" Error while loading Alert Data" + e);
            return null;
        }
    }


    @Transactional
    public String assignMaintenance(String machineId) {
            MaintenanceAlert alert = getAlertData(machineId);
            if (alert.getMaintenanceStatus().equals(MaintenanceStatus.STARTED) ||
                    alert.getMaintenanceStatus().equals(MaintenanceStatus.DONE) ){
                return "Maintenance is  Already Assigned to " + alert.getTechnician();
            }
            alert.setMaintenanceStatus(MaintenanceStatus.STARTED);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            alert.setTechnician(username);
            repository.save(alert);
            connectedUsersStore.setTechnicianStatus(username);
            machineStateStore.setStatus(machineId,MachineAlertStatus.ASSIGNED);
            return "Maintainance is Assigned to "+ username;
    }

    public String completeMaintenance(String machineId) {
        MaintenanceAlert alert = getAlertData(machineId);
        if (alert.getMaintenanceStatus().equals(MaintenanceStatus.PENDING) ||
                alert.getMaintenanceStatus().equals(MaintenanceStatus.DONE) ){
            return "Wrong Machine " + alert.getMachineID();
        }
        alert.setMaintenanceStatus(MaintenanceStatus.DONE);
        machineStateStore.clearAlert(machineId);
        connectedUsersStore.resetTechnicianStatus(alert.getTechnician());
        return "done";
    }
}
