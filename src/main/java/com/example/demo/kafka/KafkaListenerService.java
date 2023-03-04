package com.example.demo.kafka;

import com.example.demo.dto.UserRequest;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerService {

    private final UserService userService;

    Logger LOG = LoggerFactory.getLogger(KafkaListenerService.class);

    @Autowired
    public KafkaListenerService(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(id = "1",topics = "stringTopic")
    void listener(String data) {
        LOG.info(data);
    }

    @KafkaListener(id = "2",topics = "userTopic")
    void listener2(UserRequest user) {
        userService.addNewUser(user);
    }
}
