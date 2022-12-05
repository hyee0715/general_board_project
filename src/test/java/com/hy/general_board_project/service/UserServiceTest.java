package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void joinUser() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                .username("김이름")
                .nickname("닉네임")
                .password("abcd123")
                .email("aa@aa")
        //        .role(Role.USER)
                .build();

        //Long userId = userService.joinUser(requestDto);

        //System.out.println(userId);
    }
}