package net.engineeringdigest.journalApp.Sevcie;

import net.engineeringdigest.journalApp.Scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTest {

    @Autowired
    private UserScheduler userScheduler;

    @Test
    @Disabled
    public void sendailFetchuser(){
    //    userScheduler.fetchUserandsendsamail();   commetn temp for testing
    }
}
