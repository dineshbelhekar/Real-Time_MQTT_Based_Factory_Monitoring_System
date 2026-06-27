package com.example.IM.PT.DataCache;

import com.example.IM.PT.Responce.UserSessionInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectedUsersStore {

    private final ConcurrentHashMap<String, String> sessionToUser =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, UserSessionInfo> userData =
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
}