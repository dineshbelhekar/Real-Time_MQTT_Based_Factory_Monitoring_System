package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.MachineData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineDataRepository extends JpaRepository<MachineData, String> {
    List<MachineData> findByDepartmentOrderByTimeStampDesc(String department);
}

