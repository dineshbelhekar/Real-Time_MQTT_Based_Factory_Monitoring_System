package com.example.IM.PT.service;

import com.example.IM.PT.repository.DepartmentDataRepository;
import com.example.IM.PT.repository.PlantDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {

    @Autowired
    private PlantDataRepository plantRepo;

    @Autowired
    private DepartmentDataRepository deptRepo;

    public Double getPlantPowerLast24Hours() {
        return plantRepo.sumPlantPowerLast24Hours(
                LocalDateTime.now().minusHours(24)
        );
    }

    public Double getDepartmentPowerLast24Hours(String dept) {
        return deptRepo.sumDepartmentPowerLast24Hours(
                dept,
                LocalDateTime.now().minusHours(24)
        );
    }
}
