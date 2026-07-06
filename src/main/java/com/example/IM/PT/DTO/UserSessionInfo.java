package com.example.IM.PT.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionInfo {

    private String userId;
    private String username;
    private String department;
    private int machineStart;
    private int machineEnd;
    private String sessionId;

    public UserSessionInfo(String userId, String username,
                           String department,
                           int machineStart,
                           int machineEnd,
                           String sessionId) {

        this.userId = userId;
        this.username = username;
        this.department = department;
        this.machineStart = machineStart;
        this.machineEnd = machineEnd;
        this.sessionId = sessionId;
    }


}
