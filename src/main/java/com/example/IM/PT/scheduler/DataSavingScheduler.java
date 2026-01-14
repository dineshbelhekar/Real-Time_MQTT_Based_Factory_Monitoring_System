package com.example.IM.PT.scheduler;

import com.example.IM.PT.DataCache.AggregationCache;
import com.example.IM.PT.Entity.DepartmentData;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Entity.PlantData;
import com.example.IM.PT.aggregation.MachineAggregation;
import com.example.IM.PT.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@Component
public class DataSavingScheduler {

    @Autowired
    private DataStorageService dataStorageService;


    private final AggregationCache cache;

        public  DataSavingScheduler (AggregationCache cache) {
            this.cache = cache;

        }

        @Scheduled(cron = "0 */3 * * * *") // every 30 minutes
        public void saveAggregatedData() {

            LocalDateTime windowEnd = LocalDateTime.now();
            Map<String, Double> deptPower = new HashMap<>();
            Map<String, Integer> deptProduction = new HashMap<>();

            double plantPower = 0;
            int plantProduction = 0;

            for (var entry : cache.getAll().entrySet()) {

                String machineId = entry.getKey();
                MachineAggregation agg = entry.getValue();

                double power = agg.getAvgPower();
                int production = agg.getLastUnitProduction();
                String dept = agg.getDepartment();

                // ---- Machine level save ----
                dataStorageService.saveMachineData(new MachineData(
                         machineId ,dept,production,power,windowEnd
                ));


                deptPower.merge(dept, power, Double::sum);
                deptProduction.merge(dept, production, Integer::sum);

                plantPower += power;
                plantProduction += production;

                agg.reset();
            }


            for (String dept : deptPower.keySet()) {
                dataStorageService.saveDapartmentData(new DepartmentData(
                        dept,
                        deptPower.get(dept),
                        deptProduction.get(dept),
                        windowEnd
                ));
            }


                dataStorageService.saveplantData(new PlantData(
                    null, plantPower, plantProduction, windowEnd
            ));
        }
    }



