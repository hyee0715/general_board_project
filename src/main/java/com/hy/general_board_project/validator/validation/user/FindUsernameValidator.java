package com.hy.general_board_project.validator.validation.user;

import com.hy.general_board_project.domain.dto.user.FindUsernameDto;
import com.hy.general_board_project.service.UserFindService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FindUsernameValidator implements ConstraintValidator<FindUsername, FindUsernameDto> {

    @Autowired
    UserFindService userFindService;

    @Override
    public void initialize(FindUsername constraintAnnotation) {

    }

    public boolean validateRealName(String realName) {
        if (realName.isEmpty() || realName.length() < 2 || realName.length() > 6) {
            return false;
        }

        return Pattern.matches("^[가-힣]{2,6}$", realName);
    }

    public boolean validateEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }

        return Pattern.matches("^(.+)@(.+)$", email);
    }

    @Override
    public boolean isValid(FindUsernameDto findUsernameDto, ConstraintValidatorContext context) {
        if (validateRealName(findUsernameDto.getRealName())
                && validateEmail(findUsernameDto.getEmail())
                && !userFindService.existsUserByRealNameAndEmail(findUsernameDto.getRealName(), findUsernameDto.getEmail())) {
            return false;
        }

        return true;
    }
}
