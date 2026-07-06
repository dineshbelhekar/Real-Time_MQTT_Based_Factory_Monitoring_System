package com.example.IM.PT.DataCache;

import com.example.IM.PT.DTO.UserSessionInfo;
import com.example.IM.PT.service.RedisService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectedUsersStore {

    private final RedisService redisService;

    private final ConcurrentHashMap<String, String> sessionToUser =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, UserSessionInfo> userData =
            new ConcurrentHashMap<>();

    public ConnectedUsersStore(RedisService redisService) {
        this.redisService = redisService;
    }


    public void add(String sessionId, UserSessionInfo info) {

        sessionToUser.put(sessionId, info.getUsername());

        userData.put(info.getUsername(), info);
    }

    public void removeBySession(String sessionId) {

        String username = sessionToUser.remove(sessionId);
        if (username != null) {
            userData.remove(username);
        }
    }

    public Collection<UserSessionInfo> getAllUsers() {
        return userData.values();
    }

    public void addTechnician(String username, String sessionId){
        sessionToUser.put(sessionId,username);
        Boolean status = redisService.getTechnicianStatus(username);
        if (status == null){
            redisService.saveTechnicianStatus(username,true);
        }

    }

    public void removeTechnicianBySession(String sessionId){

        String username = sessionToUser.get(sessionId);

        if (username != null && redisService.getTechnicianStatus(username)){
            redisService.removeTechnicianStatus(username);
        }
    }

    public Map<String, Boolean> getAllTechnicians(){
        return redisService.getAllTechnicians();
    }

    public void setTechnicianStatus(String username){//technicianStatus.put(username,false);
        redisService.saveTechnicianStatus(username,false);
    }

    public void resetTechnicianStatus(String username){
        redisService.saveTechnicianStatus(username,true);

    }

}