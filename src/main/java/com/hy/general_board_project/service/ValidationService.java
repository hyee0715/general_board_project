package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.dto.user.*;
import com.hy.general_board_project.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ValidationService {

    private final UserFindService userFindService;

    public BindingResult validateNicknameForUpdateUserInfo(UserInfoUpdateRequestDto userInfoUpdateRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userInfoUpdateRequestDto.getNickname())) {
            bindingResult.rejectValue("nickname", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,10}$", userInfoUpdateRequestDto.getNickname())) {
                bindingResult.addError(new FieldError("userInfoUpdateRequestDto", "nickname", userInfoUpdateRequestDto.getNickname(), false, null, null, "닉네임은 특수문자 미포함 2~10자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateCurrentPasswordForUpdatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, BindingResult bindingResult, User currentUser) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "required", "");
        } else {
            if (!encoder.matches(userPasswordUpdateRequestDto.getCurrentPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "currentPassword", userPasswordUpdateRequestDto.getCurrentPassword(), false, null, null, "비밀번호가 일치하지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateNewPasswordForUpdatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, BindingResult bindingResult, User currentUser) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getNewPassword())) {
            bindingResult.rejectValue("newPassword", "required", "");
        } else {
            //현재 비밀번호와 동일한 비밀번호를 입력했을 경우
            if (encoder.matches(userPasswordUpdateRequestDto.getNewPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPassword", userPasswordUpdateRequestDto.getNewPassword(), false, null, null, "기존과 동일한 비밀번호 입니다."));
            } else if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", userPasswordUpdateRequestDto.getNewPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPassword", userPasswordUpdateRequestDto.getNewPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateNewPasswordConfirmForUpdatePassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getNewPasswordConfirm())) {
            bindingResult.rejectValue("newPasswordConfirm", "required", "");
        } else {
            if (!userPasswordUpdateRequestDto.getNewPassword().equals(userPasswordUpdateRequestDto.getNewPasswordConfirm())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPasswordConfirm", userPasswordUpdateRequestDto.getNewPasswordConfirm(), false, null, null, "새 비밀번호와 일치하지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validatePasswordForWithdrawal(FormUserWithdrawRequestDto formUserWithdrawRequestDto, BindingResult bindingResult, User currentUser) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!StringUtils.hasText(formUserWithdrawRequestDto.getRequestPassword())) {
            bindingResult.rejectValue("requestPassword", "required", "");
        } else {
            if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", formUserWithdrawRequestDto.getRequestPassword())) {
                bindingResult.addError(new FieldError("formUserWithdrawRequestDto", "requestPassword", formUserWithdrawRequestDto.getRequestPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            } else if (!encoder.matches(formUserWithdrawRequestDto.getRequestPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("formUserWithdrawRequestDto", "requestPassword", formUserWithdrawRequestDto.getRequestPassword(), false, null, null, "비밀번호가 일치하지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateEmailForWithdrawal(SocialUserWithdrawRequestDto socialUserWithdrawRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(socialUserWithdrawRequestDto.getRequestEmail())) {
            bindingResult.rejectValue("requestEmail", "required", "");
        } else {
            if (!Pattern.matches("^(.+)@(.+)$", socialUserWithdrawRequestDto.getRequestEmail())) {
                bindingResult.addError(new FieldError("socialUserWithdrawRequestDto", "requestEmail", socialUserWithdrawRequestDto.getRequestEmail(), false, null, null, "이메일 형식에 맞지 않습니다."));
            } else if (!socialUserWithdrawRequestDto.getEmail().equals(socialUserWithdrawRequestDto.getRequestEmail())) {
                bindingResult.addError(new FieldError("socialUserWithdrawRequestDto", "requestEmail", socialUserWithdrawRequestDto.getRequestEmail(), false, null, null, "이메일이 일치하지 않습니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateRealNameForSignUp(UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userSignUpRequestDto.getRealName())) {
            bindingResult.rejectValue("realName", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣]{2,6}$", userSignUpRequestDto.getRealName())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "realName", userSignUpRequestDto.getRealName(), false, null, null, "이름은 한글로 구성된 2~6자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateUsernameForSignUp(UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userSignUpRequestDto.getUsername())) {
            bindingResult.rejectValue("username", "required", "");
        } else {
            if (!Pattern.matches("^[a-z0-9]{4,20}$", userSignUpRequestDto.getUsername())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "username", userSignUpRequestDto.getUsername(), false, null, null, "아이디는 영어 소문자, 숫자 포함 4~20자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validateNicknameForSignUp(UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userSignUpRequestDto.getNickname())) {
            bindingResult.rejectValue("nickname", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,10}$", userSignUpRequestDto.getNickname())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "nickname", userSignUpRequestDto.getNickname(), false, null, null, "닉네임은 특수문자 미포함 2~10자리 제한입니다."));
            }
        }

        return bindingResult;
    }

    public BindingResult validatePasswordForSignUp(UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {
        if (!StringUtils.hasText(userSignUpRequestDto.getPassword())) {
            bindingResult.rejectValue("password", "required", "");
        } else {
            if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", userSignUpRequestDto.getPassword())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "password", userSignUpRequestDto.getPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            }
        }

        return bindingResult;
    }

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
