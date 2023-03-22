package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void joinUser() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                .realName("김이름")
                .username("아이디")
                .nickname("닉네임")
                .password("abcd123")
                .email("aa@aa")
                .build();

        Long userId = userService.joinUser(requestDto);

        //Assertions.assertThat(userId).isEqualTo(203);
    }
}