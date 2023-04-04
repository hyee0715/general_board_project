package com.hy.general_board_project.validator.validation.setting;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {

    @Autowired
    SettingService settingService;

    @Override
    public void initialize(CurrentPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        User currentUser = settingService.findUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(value, currentUser.getPassword())) {
            return false;
        }
        return true;
    }
}