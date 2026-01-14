package com.example.IM.PT.repository;

import com.example.IM.PT.Entity.PlantData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantDataRepository extends JpaRepository<PlantData,Integer> {

    PlantData findTopByOrderByTimeStampDesc();


}
