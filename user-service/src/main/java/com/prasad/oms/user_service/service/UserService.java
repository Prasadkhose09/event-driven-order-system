package com.prasad.oms.user_service.service;

import com.prasad.oms.user_service.entity.User;
import com.prasad.oms.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
