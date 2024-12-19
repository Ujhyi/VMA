package com.example.backend.SpringServer.rest_controller;

import com.example.backend.SpringServer.models.Login;
import com.example.backend.SpringServer.models.User;
import com.example.backend.SpringServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LoginApiController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Login login) {
        List<String> userEmail = userService.checkUserEmail(login.getEmail());

        if (userEmail.isEmpty()) {
            return new ResponseEntity<>("Email does not exist", HttpStatus.NOT_FOUND);
        }

        String tempPassword = userService.checkUserPasswordByEmail(login.getEmail());
        if (tempPassword.matches(login.getPassword())) {
            User user = userService.getUserDetailsByEmail(login.getEmail());
            // Include user ID, school, and ISIC in the response
            return new ResponseEntity<>(new UserResponse(user.getUser_id(), user.getUsername(), user.getEmail(), user.getSchool(), user.getIsic()), HttpStatus.OK);
        }

        return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
    }
}

// Create a response class for user details
class UserResponse {
    private int user_id;
    private String username;
    private String email;
    private String school; // Add this
    private String isic;   // Add this

    public UserResponse(int user_id, String username, String email, String school, String isic) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.school = school; // Add this
        this.isic = isic;     // Add this
    }

    // Getters

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getSchool() {
        return school; // Add this
    }

    public String getIsic() {
        return isic;   // Add this
    }
}