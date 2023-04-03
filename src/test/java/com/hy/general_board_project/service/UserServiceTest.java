package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    UserSignUpRequestDto requestDto;
    Long userId;

    @BeforeEach
    public void signUp() {
        requestDto = UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("testUser1")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aabc@aabcd.com")
                        .build();

        userId = userService.joinUser(requestDto);
    }

    @Test
    @DisplayName("가입하기")
    @Transactional
    void joinUser() {
        Long result = userRepository.findById(userId).get().getId();

        Assertions.assertThat(result).isEqualTo(userId);
    }

    @Test
    @DisplayName("이메일 인증 처리 하기")
    @Transactional
    public void emailCertifiedUpdate() {
        userService.emailCertifiedUpdate(requestDto);
        String result = userRepository.findById(userId).get().getCertified();

        Assertions.assertThat(result).isEqualTo("Y");
    }
}
