package com.prasad.oms.user_service.service;

import com.prasad.oms.user_service.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    void deleteUser(Long userId);
    UserDTO getUser(Long userId);


}
