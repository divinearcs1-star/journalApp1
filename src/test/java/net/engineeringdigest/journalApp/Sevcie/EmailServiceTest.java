package net.engineeringdigest.journalApp.Sevcie;


import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

 //   @Test
    public void testmail(){
        emailService.sendMail("innameoffking@gmail.com",
                "Testing mail sender"
        ,"Hii, Aap kaise he!!!");
    }


}
