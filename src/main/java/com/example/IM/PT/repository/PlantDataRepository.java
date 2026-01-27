package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.PlantData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PlantDataRepository extends JpaRepository<PlantData,Integer> {

    PlantData findTopByOrderByTimeStampDesc();

    @Query("""
        SELECT COALESCE(SUM(p.totalPower), 0)
        FROM PlantData p
        WHERE p.timeStamp >= :fromTime
    """)
    Double sumPlantPowerLast24Hours(@Param("fromTime") LocalDateTime fromTime);




}
