package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {


    @Autowired
    private EmailService emailService;

//    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")   temp comment for testing
    public void consume(SentimentData sentimentData){
        sendMail(sentimentData);
    }
    private void sendMail(SentimentData sentimentData){
        emailService.sendMail(sentimentData.getEmail(), "sentiment for precious week" , sentimentData.getSentiment());
    }
}
