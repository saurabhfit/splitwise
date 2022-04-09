package com.splitwise.controller;

import com.splitwise.model.DTO.UserDTO;
import com.splitwise.model.User;
import com.splitwise.model.payload.ResponsePayload;
import com.splitwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/1.0/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addUser", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePayload> addUser(@RequestBody UserDTO userDTO){
        ResponsePayload payload = new ResponsePayload();
        if(userDTO.getUserName().isEmpty() ||
                userDTO.getEmail().isEmpty() ||
                userDTO.getMobileNumber().isEmpty()){
            payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.FAILURE);
            payload.setResponseMessage("Incorrect parameters");
            return ResponseEntity.badRequest().body(payload);
        }else{
            User user = userService.saveUser(userDTO);
            if(user!=null && !user.getId().isEmpty()){
                payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.SUCCESS);
                payload.setResponseMessage("User details saved successfully");
                return ResponseEntity.ok().body(payload);
            }else{
                payload.setResponseStatus(ResponsePayload.RESPONSE_STATUS.FAILURE);
                payload.setResponseMessage("Unable to save user details");
                return ResponseEntity.internalServerError().body(payload);
            }
        }
    }
}
