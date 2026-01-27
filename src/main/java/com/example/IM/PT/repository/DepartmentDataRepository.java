package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.DepartmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface DepartmentDataRepository extends JpaRepository< DepartmentData,Integer> {

    DepartmentData findTopByDepartmentOrderByTimeStampDesc(String dept);

    @Query("""
        SELECT COALESCE(SUM(d.totalPower), 0)
        FROM DepartmentData d
        WHERE d.department = :dept
          AND d.timeStamp >= :fromTime
    """)
    Double sumDepartmentPowerLast24Hours(
            @Param("dept") String department,
            @Param("fromTime") LocalDateTime fromTime
    );
}
