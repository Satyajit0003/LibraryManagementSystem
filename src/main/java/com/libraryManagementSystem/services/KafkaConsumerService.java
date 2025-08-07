package com.libraryManagementSystem.services;

import com.libraryManagementSystem.dto.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email-notifications", groupId = "group-1")
    public void consumeEmailNotification(EmailEvent emailEvent){
         emailService.sendEmail(
                 emailEvent.getEmail(),
                 emailEvent.getSubject(),
                 emailEvent.getBody()
         );
         log.info("Email notification consumed: {}", emailEvent.getEmail());
    }
}
