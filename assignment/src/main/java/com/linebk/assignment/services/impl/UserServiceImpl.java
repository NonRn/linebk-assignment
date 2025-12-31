// java
package com.linebk.assignment.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linebk.assignment.models.dto.UserDto;
import com.linebk.assignment.models.entities.User;
import com.linebk.assignment.models.entities.UserGreeting;
import com.linebk.assignment.repositories.UserGreetingJpaRepository;
import com.linebk.assignment.repositories.UserJpaRepository;
import com.linebk.assignment.services.UserService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserGreetingJpaRepository userGreetingJpaRepository;

    @Override
    public UserDto getUserById(String userId) {
        log.debug("getUserById called with userId={}", userId);
        if (StringUtils.isBlank(userId)) {
            log.warn("Invalid userId provided: {}", userId);
            return null;
        }

        Optional<User> user = userJpaRepository.findById(userId);
        if(user.isEmpty()){
            log.info("No user found for id={}", userId);
            return null;
        } else {
            log.info("User found for id={}", userId);
            String greetingText = getGreetingText(userId);
            return mapToDto(user.get(), greetingText);
        }
    }

    @Override
    public List<String> getAllUserIds() {
        log.debug("getAllUserIds called");
        List<String> userIdList = userJpaRepository.getAllUserIdsLimitNative();
        log.info("Total userIds retrieved: {}", userIdList.size());
        return userIdList;
    }

    private String getGreetingText(String userId) {
        return userGreetingJpaRepository.findById(userId)
                .map(UserGreeting::getGreeting)
                .orElse(null);
    }

    private UserDto mapToDto(User user, String greetingText) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .greetingText(greetingText)
                .build();
    }
}