package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.dto.UserDTO;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveEntry (User user){
        userRepository.save(user);
    }

    public void saveNewUser (User user){
        try {
            user.setPassWord(passwordEncoder.encode(user.getPassWord()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        }
        catch(Exception e){
            log.debug("hahahahaahha");
            log.info("hahahahaahha");
            log.warn("hahahahaahha");
            logger.error("hahahahaahha");
        }
    }

    public void saveAdmin (User user){
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public List<User> getAll (){
        return userRepository.findAll();
    }

    public Optional<User> findById (ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteById (ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName (String userName){
        return userRepository.findByUserName(userName);
    }

}

//  controller  -> service -> repository