package com.example.IM.PT.scheduler;

import com.example.IM.PT.DataCache.ConnectedUsersStore;
import com.example.IM.PT.DataCache.MqttSubscriber;
import com.example.IM.PT.Entity.MachineData;
import com.example.IM.PT.Responce.MachineResponse;
import com.example.IM.PT.Responce.UserSessionInfo;
import com.example.IM.PT.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveDataScheduler {

    @Autowired
    private UserService userService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    private final ConnectedUsersStore store;
    private final SimpMessagingTemplate messagingTemplate;
    private final MqttSubscriber mqttSubscriber; // your existing service


   @Scheduled(fixedDelay = 2000)
    public void pushData() {

        Map<String, MachineResponse> liveData =
                mqttSubscriber.getLiveData();

       for (UserSessionInfo user : store.getAllUsers()) {
           if (user.getDepartment().equals("A")) {
               messagingTemplate.convertAndSendToUser(
                       user.getUsername(),
                       "/queue/messages",
                       liveData
               );
           } else {
               Map<String, MachineResponse> filtered =
                       liveData.entrySet()
                               .stream()
                               .filter(entry ->
                                       entry.getValue().getDepartment()
                                               .equals(user.getDepartment())
                                               &&
                                               Integer.parseInt(entry.getValue().getMachineId())
                                                       >= user.getMachineStart()
                                               &&
                                               Integer.parseInt(entry.getValue().getMachineId())
                                                       <= user.getMachineEnd()
                               )
                               .collect(Collectors.toMap(
                                       Map.Entry::getKey,
                                       Map.Entry::getValue
                               ));

               messagingTemplate.convertAndSendToUser(
                       user.getUsername(),
                       "/queue/messages",
                       filtered
               );

           }
       }
    }
}