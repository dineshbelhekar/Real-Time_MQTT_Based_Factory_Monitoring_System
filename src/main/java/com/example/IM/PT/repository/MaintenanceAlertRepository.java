package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.MaintenanceAlert;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MaintenanceAlertRepository extends JpaRepository<MaintenanceAlert, Integer> {

    MaintenanceAlert findTopByMachineIDOrderByFailTimeDesc(String machineID);
}
