package com.hy.general_board_project.validator;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class CheckUsernameValidator extends AbstractValidator<UserSignUpRequestDto>{

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserSignUpRequestDto userSignUpRequestDto, Errors errors) {
        if (!Pattern.matches("^[a-z0-9]{4,20}$", userSignUpRequestDto.getUsername())) {
            return;
        }

        if (userRepository.existsByUsername(userSignUpRequestDto.toEntity().getUsername())) {
            errors.rejectValue("username","아이디 중복 오류", "이미 사용 중인 아이디입니다.");
        }
    }
}