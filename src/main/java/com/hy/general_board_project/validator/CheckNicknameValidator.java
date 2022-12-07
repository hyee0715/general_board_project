package com.hy.general_board_project.validator;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckNicknameValidator extends AbstractValidator<UserSignUpRequestDto> {

    private final UserRepository userRepository;

    @Override
    protected void doValidate(UserSignUpRequestDto userSignUpRequestDto, Errors errors) {
        if (userRepository.existsByNickname(userSignUpRequestDto.toEntity().getNickname())) {
            errors.rejectValue("nickname","닉네임 중복 오류", "이미 사용 중인 닉네임입니다.");
        }
    }
}
