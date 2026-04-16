package net.engineeringdigest.journalApp.Scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import net.engineeringdigest.journalApp.service.UserDetailsSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Scheduled(cron = "0 0 9 * * SUN")  // for every sunday 9 am
//    @Scheduled(cron = "0 * * ? * *")    // for every minute run
    public void fetchUserandsendsamail(){
        List<User> users = userRepository.getUserforSA();
        for (User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x-> x.getSentiment()).collect(Collectors.toList());
//            String entry = String.join(" ", filterList);
//            String sentiment = sentimentAnalysisService.getSentiment(entry);
//            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", sentiment);

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
    //            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days " , mostFrequentSentiment.toString());
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendMail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
            }
        }
    }

 //   @Scheduled(cron = "0 0/5 * ? * *")    // for every 5 minute run
    public void appcache(){
     //   System.out.println("In app cache");
        appCache.init();
    }
}
