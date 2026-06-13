package com.psa.service;

import com.psa.dto.UserRequestDto;
import com.psa.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserRequestDto userRequestDto);
    User updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
}