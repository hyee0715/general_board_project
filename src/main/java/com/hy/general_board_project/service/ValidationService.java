package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.dto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ValidationService {

    private final UserFindService userFindService;

    public BindingResult validateUsernameForFinding(FindUsernameDto findUsernameDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(findUsernameDto.getRealName())) {
            bindingResult.rejectValue("realName", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣]{2,6}$", findUsernameDto.getRealName())) {
                bindingResult.addError(new FieldError("findUsernameDto", "realName", findUsernameDto.getRealName(), false, null, null, "이름은 한글로 구성된 2~6자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateEmailForFinding(FindUsernameDto findUsernameDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(findUsernameDto.getEmail())) {
            bindingResult.rejectValue("email", "required", "");
        } else {
            if (!Pattern.matches("^(.+)@(.+)$", findUsernameDto.getEmail())) {
                bindingResult.addError(new FieldError("findUsernameDto", "email", findUsernameDto.getEmail(), false, null, null, "이메일 형식에 맞지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult checkGlobalErrorForFinding(FindUsernameDto findUsernameDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && !userFindService.existsUserByRealNameAndEmail(findUsernameDto.getRealName(), findUsernameDto.getEmail())) {
            bindingResult.reject("noUser", "입력하신 정보와 일치하는 계정이 존재하지 않습니다.");
        }

        return bindingResult;
    }

    public BindingResult validateRealNameForFindingPassword(FindPasswordDto findPasswordDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(findPasswordDto.getRealName())) {
            bindingResult.rejectValue("realName", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣]{2,6}$", findPasswordDto.getRealName())) {
                bindingResult.addError(new FieldError("findPasswordDto", "realName", findPasswordDto.getRealName(), false, null, null, "이름은 한글로 구성된 2~6자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateUsernameForFindingPassword(FindPasswordDto findPasswordDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(findPasswordDto.getUsername())) {
            bindingResult.rejectValue("username", "required", "");
        } else {
            if (!Pattern.matches("^[a-z0-9]{4,20}$", findPasswordDto.getUsername())) {
                bindingResult.addError(new FieldError("findPasswordDto", "username", findPasswordDto.getUsername(), false, null, null, "아이디는 영어 소문자, 숫자 포함 4~20자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateEmailForFindingPassword(FindPasswordDto findPasswordDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(findPasswordDto.getEmail())) {
            bindingResult.rejectValue("email", "required", "");
        } else {
            if (!Pattern.matches("^(.+)@(.+)$", findPasswordDto.getEmail())) {
                bindingResult.addError(new FieldError("findPasswordDto", "email", findPasswordDto.getEmail(), false, null, null, "이메일 형식에 맞지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult checkGlobalErrorForFindingPassword(FindPasswordDto findPasswordDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors() && !userFindService.existsUserByRealNameAndUsernameAndEmail(findPasswordDto.getRealName(), findPasswordDto.getUsername(), findPasswordDto.getEmail())) {
            bindingResult.reject("noUser", "입력하신 정보와 일치하는 계정이 존재하지 않습니다.");
        }

        return bindingResult;
    }
}
