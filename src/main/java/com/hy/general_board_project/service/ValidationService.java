package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.web.dto.user.FormUserWithdrawRequestDto;
import com.hy.general_board_project.web.dto.user.SocialUserWithdrawRequestDto;
import com.hy.general_board_project.web.dto.user.UserInfoUpdateRequestDto;
import com.hy.general_board_project.web.dto.user.UserPasswordUpdateRequestDto;
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
}
