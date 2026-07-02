package com.example.IM.PT.DataCache;

import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.service.MaintenanceAlertService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class MqttSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AggregationCache aggregationCache;
    private final MaintenanceAlertService maintenanceAlertService;

    public MqttSubscriber(AggregationCache aggregationCache, MaintenanceAlertService maintenanceAlertService) {
        this.aggregationCache = aggregationCache;
        this.maintenanceAlertService = maintenanceAlertService;
    }

    //private MachineResponse data;

    private final ConcurrentHashMap<String, MachineResponse> liveData =
            new ConcurrentHashMap<>();


    public void handleMessage(String topic, String payload) {
        try {
            String[] parts = topic.split("/");


            String department = parts[1];
            String machineId = parts[2];

            MachineResponse data =
                    objectMapper.readValue(payload, MachineResponse.class);

            data.setDepartment(department);
            data.setMachineId(machineId);

            if (!data.getCondition()) {

                maintenanceAlertService.handleMachineFailure(data);

            } else {

                maintenanceAlertService.handleMachineRecovery(data);
            }

            liveData.put(machineId, data);
            aggregationCache.aggregate(data);
           // System.out.println("Received from " + department + " | " + machineId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, MachineResponse> getLiveData() {
        return liveData;
    }

}
