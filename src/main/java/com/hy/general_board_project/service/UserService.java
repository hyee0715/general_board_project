package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long joinUser(UserSignUpRequestDto userSignUpRequestDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userSignUpRequestDto.setPassword(bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword()));

        userSignUpRequestDto.setRole(Role.USER);

        return userRepository.save(userSignUpRequestDto.toEntity()).getId();
    }
}