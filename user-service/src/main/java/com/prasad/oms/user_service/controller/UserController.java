package com.prasad.oms.user_service.controller;


import com.prasad.oms.user_service.entity.User;
import com.prasad.oms.user_service.service.UserService;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public User createUser(@RequestBody User user){
        return service.saveUser(user);

    }

    @GetMapping
    public List<User> getUsers(){
        return service.getAllUsers();
    }
}
