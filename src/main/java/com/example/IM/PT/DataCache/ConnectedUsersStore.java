package com.example.IM.PT.DataCache;

import com.example.IM.PT.DTO.UserSessionInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectedUsersStore {

    private final ConcurrentHashMap<String, String> sessionToUser =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, UserSessionInfo> userData =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Boolean> technicianStatus =
            new ConcurrentHashMap<>();

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
        technicianStatus.put(username,true);
    }

    public void removeTechnicianBySession(String sessionId){

        String username = sessionToUser.remove(sessionId);

        if (username != null) {
            technicianStatus.remove(username);
        }
    }

    public ConcurrentHashMap<String, Boolean> getAllTechnicians(){
        return technicianStatus;
    }

    public ConcurrentHashMap<String, String> getsessiontouser(){
        return sessionToUser;
    }

    public void setTechnicianStatus(String username){
        technicianStatus.put(username,false);
    }

    public void resetTechnicianStatus(String username){
        technicianStatus.put(username,true);
    }
}