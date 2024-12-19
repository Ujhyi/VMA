package com.example.backend.SpringServer.services;

import com.example.backend.SpringServer.models.User;
import com.example.backend.SpringServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public int registerNewUserSeviceMethod(String fusername, String femail, String password, String fisic, String fschool){
        return userRepository.registerNewUser(fusername, femail, password, fisic, fschool);
    }

    // CHECK EMAIL SERVICE METHOD:
    public List<String> checkUserEmail(String email)
    {
        return userRepository.checkUserEmail(email);
    }

    //END OF CHECK USER EMAIL METHOD SERVICE
    public String checkUserPasswordByEmail(String email)
    {
        return userRepository.checkUserPasswordByEmail(email);
    }

    //END OF CHECK USER PASSWORD METHOD SERVICE
    public User getUserDetailsByEmail(String email)
    {
        return userRepository.GetUserDetailsByEmail(email);
    }


}
