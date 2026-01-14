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


    public PlantData getAllPlantData() throws Exception
    {
        try {

            return plantDataRepository.findTopByOrderByTimeStampDesc();

        } catch (DataAccessException e) {
            log.error("Failed to fetch department data ",e);
            throw new Exception("Unable to load department data ",e);
        }
    }

    public List<DepartmentData> getAllDepartmentData() throws Exception {
        try {
            return departmentDataRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch department data ", e);
            throw new Exception("Unable to load department data ",e);
        }
    }

    public List<MachineData> getAllMachineData() throws Exception {

        try {
            return machineDataRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch Machine data ", e);
            throw new Exception("Unable to load Machine data ", e);
        }

    }



    public DepartmentData getDepartmentData(String Department) throws Exception {
        try {
            return departmentDataRepository.findTopByDepartmentOrderByTimeStampDesc(Department);
        } catch (DataAccessException e) {
            log.error("Failed to fetch department data ", e);
            throw new Exception("Unable to load department data ",e);
        }
    }

    public List<MachineData> getDeptMachineData(String Department) throws Exception {
        try {
            return machineDataRepository.findByDepartmentOrderByTimeStampDesc(Department);
        } catch (DataAccessException e) {
            log.error("Failed to fetch Machine data ", e);
            throw new Exception("Unable to load Machine data ", e);
        }
    }

}
