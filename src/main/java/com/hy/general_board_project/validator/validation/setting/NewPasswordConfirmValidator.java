package com.hy.general_board_project.validator.validation.setting;

import com.hy.general_board_project.domain.dto.user.UserPasswordUpdateRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NewPasswordConfirmValidator implements ConstraintValidator<NewPasswordConfirm, UserPasswordUpdateRequestDto> {

    @Override
    public void initialize(NewPasswordConfirm constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, ConstraintValidatorContext context) {
        return userPasswordUpdateRequestDto.isValidNewPasswordConfirm();
    }
}
