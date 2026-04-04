package net.engineeringdigest.journalApp.Sevcie;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisteest(){
        redisTemplate.opsForValue().set("email","qwerty@email.com");
        Object salary = redisTemplate.opsForValue().get("salary");
        int a=1;
    }

}
