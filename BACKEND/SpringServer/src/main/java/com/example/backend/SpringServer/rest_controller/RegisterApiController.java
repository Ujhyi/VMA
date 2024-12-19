package com.example.backend.SpringServer.rest_controller;

import com.example.backend.SpringServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterApiController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity registerNewUser(@RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("isic") String isic,
                                          @RequestParam("school") String school){

        if (username.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            return new ResponseEntity<>("Please Complete all Fields", HttpStatus.BAD_REQUEST);
        }

        int result = userService.registerNewUserSeviceMethod(username, email, password, isic, school);

        if(result != 1)
        {
            return new ResponseEntity<>("Failed to Register User", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
    }


}
