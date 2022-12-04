package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public Long joinUser(UserDto userDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userDto.setRole(Role.USER);

        return userRepository.save(userDto.toEntity()).getId();
    }
}