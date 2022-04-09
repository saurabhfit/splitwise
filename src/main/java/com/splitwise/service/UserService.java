package com.splitwise.service;

import com.splitwise.Repository.UserRepository;
import com.splitwise.model.DTO.UserDTO;
import com.splitwise.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(UserDTO userDTO) {
        if(userRepository.findByUserName(userDTO.getUserName())!=null ||
            userRepository.findByEmail(userDTO.getEmail())!=null ||
            userRepository.findByMobileNumber(userDTO.getMobileNumber())!=null){
            return null;
        }
        User user = new User(userDTO.getUserName(), userDTO.getEmail(), userDTO.getMobileNumber());
        try{
            userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
