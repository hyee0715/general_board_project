package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    List<Long> afterDeleteIds = new ArrayList<>();

    @AfterEach
    void deleteAfterTest() {
        for (long id : afterDeleteIds) {
            userRepository.deleteById(id);
        }

        afterDeleteIds.clear();
    }

    @Test
    @DisplayName("가입하기")
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
        Long result = userRepository.findById(userId).get().getId();

        afterDeleteIds.add(userId);

        Assertions.assertThat(result).isEqualTo(userId);
    }

    @Test
    @DisplayName("이메일 인증 처리 하기")
    public void emailCertifiedUpdate() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("아이디")
                        .nickname("닉네임")
                        .password("abcd123")
                        .email("aa@aa")
                        .build();

        Long userId = userService.joinUser(requestDto);
        afterDeleteIds.add(userId);

        userService.emailCertifiedUpdate(requestDto);
        String result = userRepository.findById(userId).get().getCertified();

        Assertions.assertThat(result).isEqualTo("Y");
    }
}
