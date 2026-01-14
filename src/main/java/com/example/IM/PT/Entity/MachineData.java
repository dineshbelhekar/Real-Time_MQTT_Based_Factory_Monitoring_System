package com.example.IM.PT.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class MachineData  {

    @Id
    @Column(unique = true,nullable = false)
    private String machineId;

    private String department;

    private Integer unitProduction;

    private double powerUsage;

    private LocalDateTime timeStamp;

}
