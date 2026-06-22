package com.example.IM.PT.scheduler;

import com.example.IM.PT.DataCache.MqttSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LiveDataScheduler {

    private final SimpMessagingTemplate messagingTemplate;
    private final MqttSubscriber mqttSubscriber; // your existing service

    @Scheduled(fixedDelay = 1000) // push every 5 seconds
    public void pushLiveData() {
        var liveData = mqttSubscriber.getLiveData(); // your existing method
        messagingTemplate.convertAndSend("/topic/messages", liveData);
    }
}