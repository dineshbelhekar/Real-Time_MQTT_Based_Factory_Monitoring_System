package com.example.IM.PT.service;

import com.example.IM.PT.Entity.DepartmentData;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Entity.PlantData;
import com.example.IM.PT.repository.DepartmentDataRepository;
import com.example.IM.PT.repository.MachineDataRepository;
import com.example.IM.PT.repository.PlantDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DataStorageService {

    private final MachineDataRepository machineRepo;
    private final DepartmentDataRepository deptRepo;
    private final PlantDataRepository plantRepo;

    public DataStorageService(
            MachineDataRepository machineRepo,
            DepartmentDataRepository deptRepo,
            PlantDataRepository plantRepo
    ) {
        this.machineRepo = machineRepo;
        this.deptRepo = deptRepo;
        this.plantRepo = plantRepo;
    }

    public void saveMachineData(MachineData machineData) {
        try {
            machineRepo.save(machineData);
        } catch (Exception e) {
            log.error("Machine Data is not Saved");
        }
    }

    public void saveDapartmentData(DepartmentData departmentData) {
        try {
            deptRepo.save(departmentData);
        } catch (Exception e) {
            log.error("Department Data is not Saved");
        }
    }

    public void saveplantData(PlantData plantData)
    {

        try {
            plantRepo.save(plantData);
        } catch (Exception e) {
            log.error("Plant Data is not Saved");
        }
    }

}
