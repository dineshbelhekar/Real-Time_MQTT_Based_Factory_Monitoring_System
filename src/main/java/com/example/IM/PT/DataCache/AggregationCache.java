package com.example.IM.PT.DataCache;

import com.example.IM.PT.MQTTResponce.MachineResponse;
import com.example.IM.PT.aggregation.MachineAggregation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class AggregationCache {

    private final ConcurrentHashMap<String, MachineAggregation> map =
            new ConcurrentHashMap<>();

    public void aggregate(MachineResponse data) {
        map.computeIfAbsent(data.getMachineId(), k -> new MachineAggregation())
                .addSample(
                        data.getVoltage(),
                        data.getCurrent(),
                        data.getUnitProduction(),
                        data.getDepartment()
                );
    }

    public Map<String, MachineAggregation> getAll() {
        return map;
    }
}
