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
    public Long joinUser(UserSignUpRequestDto requestDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        requestDto.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));

        requestDto.setRole(Role.USER);

        return userRepository.save(requestDto.toEntity()).getId();
    }
}