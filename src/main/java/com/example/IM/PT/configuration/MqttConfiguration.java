package com.example.IM.PT.configuration;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
        @Bean
        public MqttClient mqttClient() throws Exception {
            MqttClient client = new MqttClient(
                    "tcp://broker.hivemq.com:1883",
                    MqttClient.generateClientId()
            );
            client.connect();
            return client;
        }
    }

