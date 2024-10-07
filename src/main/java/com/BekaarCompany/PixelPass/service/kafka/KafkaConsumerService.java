package com.BekaarCompany.PixelPass.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my_topic",groupId = "group_id")
    public void listen(String message){
        System.out.println("Recieved Message!");
    }
}
