package com.example.IM.PT.configuration;

import com.example.IM.PT.DataCache.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

@Component
public class Mqttinitializer {

    public Mqttinitializer(MqttClient client, MqttSubscriber subscriber)
            throws Exception {

        client.subscribe("plant/+/+/data",
                (topic, msg) -> subscriber.handleMessage(
                        topic,
                        new String(msg.getPayload())
                )
        );

    }
}

