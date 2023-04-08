package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.domain.dto.user.FindPasswordDto;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class UserFindServiceTest {

    @Autowired
    UserFindService userFindService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    UserSignUpRequestDto requestDto;
    Long userId;
    User user;

    @BeforeEach
    public void signUp() {
        requestDto = UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("testUser1")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aabc@aabcd.com")
                        .certified("Y")
                        .build();

        userId = userService.joinUser(requestDto);
        user = userRepository.findById(userId).get();
    }

    @Test
    @DisplayName("이름과 이메일로 계정을 찾을 때 계정이 존재하는 경우")
    @Transactional
    public void existsUserByRealNameAndEmail() {
        boolean result = userFindService.existsUserByRealNameAndEmail("김이름", "aabc@aabcd.com");

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이름과 이메일로 계정을 찾을 때 계정이 존재하지 않는 경우")
    @Transactional
    public void notExistsUserByRealNameAndEmail() {
        boolean result = userFindService.existsUserByRealNameAndEmail("안이름", "abc@aabcd.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이름과 아이디와 이메일로 계정을 찾을 때 계정이 존재하는 경우")
    @Transactional
    public void existsUserByRealNameAndUsernameAndEmail() {
        boolean result = userFindService.existsUserByRealNameAndUsernameAndEmail("김이름", "testUser1", "aabc@aabcd.com");

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이름과 아이디와 이메일로 계정을 찾을 때 계정이 존재하지 않는 경우")
    @Transactional
    public void notExistsUserByRealNameAndUsernameAndEmail() {
        boolean result = userFindService.existsUserByRealNameAndUsernameAndEmail("안이름", "testUser", "abc@aabcd.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 특수문자가 포함되어 있는 경우")
    @Transactional
    public void validatePasswordFormatContainsSpecialCharacter() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('!');
        set.add('A');
        set.add(']');

        boolean result = userFindService.validatePasswordFormatContainsSpecialCharacter(set);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 특수문자가 포함되어 있지 않은 경우")
    @Transactional
    public void notValidatePasswordFormatContainsSpecialCharacter() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('z');
        set.add('A');
        set.add('0');

        boolean result = userFindService.validatePasswordFormatContainsSpecialCharacter(set);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 숫자가 포함되어 있는 경우")
    @Transactional
    public void validatePasswordFormatContainsDigit() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('!');
        set.add('A');
        set.add('9');

        boolean result = userFindService.validatePasswordFormatContainsDigit(set);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 숫자가 포함되어 있지 않은 경우")
    @Transactional
    public void notValidatePasswordFormatContainsDigit() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('b');
        set.add('!');
        set.add('A');
        set.add(']');

        boolean result = userFindService.validatePasswordFormatContainsDigit(set);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 대문자가 포함되어 있는 경우")
    @Transactional
    public void validatePasswordFormatContainsUpperCaseLetters() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('!');
        set.add('A');
        set.add('9');

        boolean result = userFindService.validatePasswordFormatContainsUpperCaseLetters(set);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 대문자가 포함되어 있지 않은 경우")
    @Transactional
    public void notValidatePasswordFormatContainsUpperCaseLetters() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('!');
        set.add('@');
        set.add('9');

        boolean result = userFindService.validatePasswordFormatContainsUpperCaseLetters(set);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 소문자가 포함되어 있는 경우")
    @Transactional
    public void validatePasswordFormatContainsLowerCaseLetters() {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('1');
        set.add('!');
        set.add('A');
        set.add('9');

        boolean result = userFindService.validatePasswordFormatContainsLowerCaseLetters(set);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("임시 비밀번호 생성 시 소문자가 포함되어 있지 않은 경우")
    @Transactional
    public void notValidatePasswordFormatContainsLowerCaseLetters() {
        Set<Character> set = new HashSet<>();
        set.add('B');
        set.add('1');
        set.add('!');
        set.add('A');
        set.add('9');

        boolean result = userFindService.validatePasswordFormatContainsLowerCaseLetters(set);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("임시 비밀번호로 비밀번호 교체하기")
    @Transactional
    public void updateUserPasswordToTempPassword() {
        String tempPassword = userFindService.makeTempPassword();

        FindPasswordDto findPasswordDto = FindPasswordDto.builder()
                .realName(requestDto.getRealName())
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .build();

        userFindService.updateUserPasswordToTempPassword(findPasswordDto, tempPassword);

        String currentPassword = userRepository.findById(userId).get().getPassword();

        Assertions.assertThat(currentPassword).isEqualTo(tempPassword);
    }
}