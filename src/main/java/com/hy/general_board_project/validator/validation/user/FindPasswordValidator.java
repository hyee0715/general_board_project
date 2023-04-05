package com.hy.general_board_project.validator.validation.user;

import com.hy.general_board_project.domain.dto.user.FindPasswordDto;
import com.hy.general_board_project.service.UserFindService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FindPasswordValidator implements ConstraintValidator<FindPassword, FindPasswordDto> {

    @Autowired
    UserFindService userFindService;

    @Override
    public void initialize(FindPassword constraintAnnotation) {

    }

    public boolean validateRealName(String realName) {
        if (realName.isEmpty() || realName.length() < 2 || realName.length() > 6) {
            return false;
        }

        return Pattern.matches("^[가-힣]{2,6}$", realName);
    }

    public boolean validateUsername(String username) {
        if (username.isEmpty() || username.length() < 4 || username.length() > 20) {
            return false;
        }

        return Pattern.matches("^([a-z0-9]){4,20}$", username);
    }

    public boolean validateEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }

        return Pattern.matches("^(.+)@(.+)$", email);
    }

    @Override
    public boolean isValid(FindPasswordDto findPasswordDto, ConstraintValidatorContext context) {
        if (validateRealName(findPasswordDto.getRealName())
                && validateUsername(findPasswordDto.getUsername())
                && validateEmail(findPasswordDto.getEmail())
                && !userFindService.existsUserByRealNameAndUsernameAndEmail(findPasswordDto.getRealName(), findPasswordDto.getUsername(), findPasswordDto.getEmail())) {
            return false;
        }

        return true;
    }
}
