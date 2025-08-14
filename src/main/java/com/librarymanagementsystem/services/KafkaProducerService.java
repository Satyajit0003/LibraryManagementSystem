package com.librarymanagementsystem.services;

import com.librarymanagementsystem.dto.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private static final String topic = "email-notifications";

    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, EmailEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceEmailNotification(EmailEvent emailEvent) {
        kafkaTemplate.send(topic, emailEvent);
        log.info("Email notification sent: {}", emailEvent.getEmail());
    }
}
