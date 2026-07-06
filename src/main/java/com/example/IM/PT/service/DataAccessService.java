package com.example.IM.PT.service;

import com.example.IM.PT.Entity.DepartmentData;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Entity.PlantData;
import com.example.IM.PT.repository.DepartmentDataRepository;
import com.example.IM.PT.repository.MachineDataRepository;
import com.example.IM.PT.repository.PlantDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class DataAccessService {


    @Autowired
    private DepartmentDataRepository departmentDataRepository;

    @Autowired
    private MachineDataRepository machineDataRepository;

    @Autowired
    private PlantDataRepository plantDataRepository;

    @Autowired
    private UserService userService;


    public PlantData getAllPlantData() {
        try {

            return plantDataRepository.findTopByOrderByTimeStampDesc();

        } catch (DataAccessException e) {
            log.error("Failed to fetch Plant data ",e);
            return null;
        }

    }

    public List<DepartmentData> getAllDepartmentData() {
        try {
            return departmentDataRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch department data ", e);
            return null;
        }
    }

    public List<MachineData> getAllMachineData()  {

        try {
            return machineDataRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch Machine data ", e);
            return null;
        }

    }

    public DepartmentData getDepartmentData() {
        try {
            String Department = userService.getUserDepartment();
            return departmentDataRepository.findTopByDepartmentOrderByTimeStampDesc(Department);
        } catch (DataAccessException e) {
            log.error("Failed to fetch department data ", e);
            return null;
        }
    }

    public List<MachineData> getDeptMachineData()  {
        try {
            String Department = userService.getUserDepartment();
            return machineDataRepository.findByDepartmentOrderByTimeStampDesc(Department);
        } catch (DataAccessException e) {
            log.error("Failed to fetch Machine data ", e);
            return null;
        }
    }

}
