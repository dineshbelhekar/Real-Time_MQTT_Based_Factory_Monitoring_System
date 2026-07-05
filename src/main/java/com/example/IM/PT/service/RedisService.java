package com.example.IM.PT.service;

import com.example.IM.PT.enums.MachineAlertStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RedisService {

    private static final String ALERT_KEY = "machine:alert:status";
    private static final String TECHNICIAN_KEY = "technician:status";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Machine Alert Status

    @Transactional
    public void saveMachineStatus(String machineId,
                                  MachineAlertStatus status) {

        redisTemplate.opsForHash()
                .put(ALERT_KEY, machineId, status);
    }

    public MachineAlertStatus getMachineStatus(String machineId) {

        try {
            Object value = redisTemplate.opsForHash().get(ALERT_KEY, machineId);
            if (value == null) {
                return null;
            }
            return MachineAlertStatus.valueOf(value.toString());
        } catch (Exception e) {
            log.error("Exception Occured while getting machine Status" + e);
            return null;
        }
    }

    public void removeMachineStatus(String machineId) {
        log.info("machine deleted " + machineId);
        redisTemplate.opsForHash()
                .delete(ALERT_KEY, machineId);
    }

    public Map<String, MachineAlertStatus> getallmachines(){
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(ALERT_KEY);

        Map<String, MachineAlertStatus> result = new HashMap<>();

        entries.forEach((key, value) -> {
            if (value != null) {
                result.put(
                        key.toString(),
                        MachineAlertStatus.valueOf(value.toString())
                );
            }
        });

        return result;
    }

    // Technician Status

    public void saveTechnicianStatus(String username,
                                     Boolean status) {

        redisTemplate.opsForHash()
                .put(TECHNICIAN_KEY, username, status);
    }

    public Boolean getTechnicianStatus(String username) {

        try {
            return (Boolean) redisTemplate
                    .opsForHash()
                    .get(TECHNICIAN_KEY, username);
        } catch (Exception e) {
            log.error("Exception Occured while getting Technician Data " + e);
            return null;
        }
    }

    public void removeTechnicianStatus(String username) {

        redisTemplate.opsForHash()
                .delete(TECHNICIAN_KEY, username);
    }

    public Map<String, Boolean> getAllTechnicians() {

        try {
            Map<Object, Object> entries =
                    redisTemplate.opsForHash().entries(TECHNICIAN_KEY);

            Map<String, Boolean> result = new HashMap<>();

            entries.forEach((key, value) ->
                    result.put((String) key, (Boolean) value));

            return result;
        } catch (Exception e) {
            log.error("Exception Occured while getting all technicians "+ e);
            return null;
        }
    }

}
