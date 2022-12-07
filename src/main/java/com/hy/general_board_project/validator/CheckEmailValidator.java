package com.hy.general_board_project.validator;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<UserSignUpRequestDto>{

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserSignUpRequestDto userSignUpRequestDto, Errors errors) {
        if (userRepository.existsByEmail(userSignUpRequestDto.toEntity().getEmail())) {
            errors.rejectValue("email","이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }
    }
}