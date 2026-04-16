package com.prasad.oms.user_service.service.impl;

import com.prasad.oms.user_service.dto.UserDTO;
import com.prasad.oms.user_service.entity.User;
import com.prasad.oms.user_service.exception.UserAlreadyExistException;
import com.prasad.oms.user_service.exception.UserNotFoundException;
import com.prasad.oms.user_service.repository.UserRepository;
import com.prasad.oms.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private com.prasad.oms.user_service.mapper.UserMapper mapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if(repository.existsByEmail(userDTO.getEmail())){
            throw new UserAlreadyExistException("User with this email already exists");

        }
        User user = mapper.toEntity(userDTO);
        User saved = repository.save(user);
        return mapper.toDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        if(!repository.existsById(userId)){
            throw new RuntimeException("User not found with this Id: "+ userId);
        }
        repository.deleteById(userId);

    }

    @Override
    public UserDTO getUser(Long userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return mapper.toDTO(user);
    }




}
