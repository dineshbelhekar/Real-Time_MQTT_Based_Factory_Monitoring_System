package com.example.IM.PT.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaintenanceAlertDTO {

    private String machineId;
    private String department;
    private String message;
    private LocalDateTime FailTime;

}