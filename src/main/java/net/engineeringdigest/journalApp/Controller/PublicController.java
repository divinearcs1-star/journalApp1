package net.engineeringdigest.journalApp.Controller;


import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Utils.JwtUtil;
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
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsSeviceImpl userDetailsSevice;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signUp")
    public void signUp (@RequestBody User user){

        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody User user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassWord()));
            UserDetails userDetails = userDetailsSevice.loadUserByUsername(user.getUserName());
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
