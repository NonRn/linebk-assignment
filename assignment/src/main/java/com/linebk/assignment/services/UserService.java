package com.linebk.assignment.services;


import com.linebk.assignment.models.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(String userId);
    List<String> getAllUserIds();
}

