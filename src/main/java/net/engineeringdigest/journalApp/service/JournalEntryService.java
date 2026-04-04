package net.engineeringdigest.journalApp.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    
    @Autowired 
    UserService userService;

    @Transactional
    public void saveEntry (JournalEntry journalEntry,String username){
        try {
            User user = userService.findByUserName(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
//            user.setUserName(null);
            userService.saveEntry(user);
        }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("Username may be null, process stopped", e);
        }
    }

    public void saveEntry (JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll (){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById (ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById (ObjectId id, String username){
        boolean removed = false;
        try {
            User user = userService.findByUserName(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println("error occured while entry");
            throw  new RuntimeException("error occured while entry",e);
        }
        return removed;
    }


}

//  controller  -> service -> repository