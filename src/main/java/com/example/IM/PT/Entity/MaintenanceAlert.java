package com.example.IM.PT.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class MaintenanceAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "machineID")
    private String machineID;

    private String department;

    private String technician;

    @Enumerated(EnumType.STRING)
    private com.example.IM.PT.enums.MaintenanceStatus MaintenanceStatus;

    private LocalDateTime failTime;

    private LocalDateTime recoverTime;

    private Duration downtime;

}
