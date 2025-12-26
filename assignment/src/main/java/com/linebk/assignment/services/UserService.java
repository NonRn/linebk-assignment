package com.linebk.assignment.services;


import com.linebk.assignment.models.dto.UserDto;

public interface UserService {

    UserDto getUserById(String userId);
}

