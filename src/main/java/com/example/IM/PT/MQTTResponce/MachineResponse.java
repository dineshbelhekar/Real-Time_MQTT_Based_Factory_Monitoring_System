package com.example.IM.PT.MQTTResponce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachineResponse {

    @JsonProperty("machineId")
    private String machineId;

    @JsonProperty("department")
    private String department;

    @JsonProperty("unitProduction")
    private Integer unitProduction;

    @JsonProperty("condition")
    private Boolean condition;

    @JsonProperty("current")
    private Float current;

    @JsonProperty("voltage")
    private Float voltage;
}
