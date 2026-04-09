package com.prasad.oms.user_service.controller;


import com.prasad.oms.user_service.dto.UserDTO;
import com.prasad.oms.user_service.entity.User;
import com.prasad.oms.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(service.createUser(userDTO));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @DeleteMapping("/deleteUser/{userId}")
    public  ResponseEntity<Void> deleteUser(@PathVariable Long  userId){
        service.deleteUser(userId);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("/getById/{userId}")
    public Optional<User> getById(@PathVariable Long userId){
        return ResponseEntity.ok(service.getUser(userId)).getBody();
    }
}