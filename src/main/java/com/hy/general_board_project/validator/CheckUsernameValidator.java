package com.hy.general_board_project.validator;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckUsernameValidator extends AbstractValidator<UserSignUpRequestDto>{

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserSignUpRequestDto userSignUpRequestDto, Errors errors) {
        if (userRepository.existsByUsername(userSignUpRequestDto.toEntity().getUsername())) {
            errors.rejectValue("username","아이디 중복 오류", "이미 사용 중인 아이디입니다.");
        }
    }
}