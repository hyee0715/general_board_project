package com.hy.general_board_project.validator.validation.setting;

import com.hy.general_board_project.domain.dto.user.SocialUserWithdrawRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithdrawEmailValidator implements ConstraintValidator<WithdrawEmail, SocialUserWithdrawRequestDto> {

    @Override
    public void initialize(WithdrawEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(SocialUserWithdrawRequestDto socialUserWithdrawRequestDto, ConstraintValidatorContext context) {
        return socialUserWithdrawRequestDto.isValidWithdrawEmail();
    }
}
