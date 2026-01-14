package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.DepartmentData;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DepartmentDataRepository extends JpaRepository< DepartmentData,String> {

    DepartmentData findTopByDepartmentOrderByTimeStampDesc(String dept);

}
