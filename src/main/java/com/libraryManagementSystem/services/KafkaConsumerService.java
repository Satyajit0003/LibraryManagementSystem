package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.Acknowledgment;

@Service
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "email-notifications", groupId = "group-1")
    public void consumeEmailNotification(EmailEvent emailEvent, Acknowledgment acknowledgment){
         emailService.sendEmail(
                 emailEvent.getEmail(),
                 emailEvent.getSubject(),
                 emailEvent.getBody()
         );
         acknowledgment.acknowledge();
         log.info("Email notification consumed: {}", emailEvent.getEmail());
    }
}
