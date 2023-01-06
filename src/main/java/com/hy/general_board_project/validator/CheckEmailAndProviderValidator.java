package com.hy.general_board_project.validator;

import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailAndProviderValidator extends AbstractValidator<UserSignUpRequestDto>{

    private final UserRepository userRepository;

    /***
     * User의 Email과 Provider를 함께 검증하여 소셜 로그인 회원과 이메일이 겹쳐도 회원가입이 가능하게 함.
     */
    @Override
    protected void doValidate(UserSignUpRequestDto userSignUpRequestDto, Errors errors) {
        if (userRepository.existsByEmailAndProvider(userSignUpRequestDto.toEntity().getEmail(), userSignUpRequestDto.toEntity().getProvider())) {
            errors.rejectValue("email","이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }
    }
}