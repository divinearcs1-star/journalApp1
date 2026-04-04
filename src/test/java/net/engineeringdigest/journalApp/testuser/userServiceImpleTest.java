package net.engineeringdigest.journalApp.testuser;


import net.engineeringdigest.journalApp.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class userServiceImpleTest {

    @Autowired
    private UserRepositoryImpl userRepositoryimpl;


    @Test
    public void test1(){
        List<User> userforSA = userRepositoryimpl.getUserforSA();
        System.out.print(userforSA.toString());

    }
}
