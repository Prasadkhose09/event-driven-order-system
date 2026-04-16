package com.prasad.oms.user_service.service;

import com.prasad.oms.user_service.dto.UserDTO;
import com.prasad.oms.user_service.entity.User;
import com.prasad.oms.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    void deleteUser(Long userId);
    Optional<User> getUser(Long userId);


}
