package net.engineeringdigest.journalApp.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Utils.JwtUtil;
import net.engineeringdigest.journalApp.dto.UserDTO;
import net.engineeringdigest.journalApp.service.UserDetailsSeviceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag( name = "Public APIs")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsSeviceImpl userDetailsSevice;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("Health-check")
    public String healthcheck(){
        log.info("Health is ok!");
        return "ok";
    }

    @PostMapping("/signup")
    public void signUp (@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassWord(user.getPassWord());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        userService.saveNewUser(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody UserDTO user){
        try{
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setPassWord(user.getPassWord());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(newUser.getUserName(), newUser.getPassWord()));
            UserDetails userDetails = userDetailsSevice.loadUserByUsername(newUser.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity <>(jwt, HttpStatus.OK);
        }
        catch(Exception e){
            e.getMessage();
            e.getClass();
            log.error("Exception occured while createauthentication token", e);
            return  new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
