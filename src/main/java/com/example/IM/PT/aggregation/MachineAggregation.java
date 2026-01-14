package com.example.IM.PT.aggregation;

import lombok.Getter;

@Getter
public class MachineAggregation {

    private double sumVoltage = 0;
    private double sumCurrent = 0;
    private int count = 0;
    private int lastUnitProduction = 0;
    private String department;

    public synchronized void addSample(float voltage, float current, int unitProduction, String department) {
        this.sumVoltage += voltage;
        this.sumCurrent += current;
        this.count++;
        this.lastUnitProduction = unitProduction;
        this.department = department;
    }

    public synchronized double getAvgPower() {
        if (count == 0) return 0;
        double avgVoltage = sumVoltage / count;
        double avgCurrent = sumCurrent / count;
        return avgVoltage * avgCurrent;
    }

    public int getLastUnitProduction() {
        return lastUnitProduction;
    }

    public String getDepartment() {
        return department;
    }

    public synchronized void reset() {
        sumVoltage = 0;
        sumCurrent = 0;
        count = 0;
    }
}
