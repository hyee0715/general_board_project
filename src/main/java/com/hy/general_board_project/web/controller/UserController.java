package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login")
    public String account() {
        return "account/login";
    }

    @GetMapping("/user/signUp")
    public String signUp(Model model) {
        model.addAttribute("userSignUpRequestDto", new UserSignUpRequestDto());
        return "account/signUp";
    }

    @PostMapping("/user/signUp")
    public String signUp(@Validated @ModelAttribute UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult) {

        if (!StringUtils.hasText(userSignUpRequestDto.getUsername())) {
            bindingResult.rejectValue("username", "required", "");
        } else {
            if (!Pattern.matches("^[a-z0-9]{4,20}$", userSignUpRequestDto.getUsername())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "username", userSignUpRequestDto.getUsername(), false, null, null, "아이디는 영어 소문자, 숫자 포함 4~20자리 제한입니다."));
            }
        }

        if (!StringUtils.hasText(userSignUpRequestDto.getNickname())) {
            bindingResult.rejectValue("nickname", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,10}$", userSignUpRequestDto.getNickname())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "nickname", userSignUpRequestDto.getNickname(), false, null, null, "닉네임은 특수문자 미포함 2~10자리 제한입니다."));
            }
        }

        if (!StringUtils.hasText(userSignUpRequestDto.getPassword())) {
            bindingResult.rejectValue("password", "required", "");
        } else {
            if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", userSignUpRequestDto.getPassword())) {
                bindingResult.addError(new FieldError("userSignUpRequestDto", "password", userSignUpRequestDto.getPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "account/signUp";
        }

        userService.joinUser(userSignUpRequestDto);
        return "redirect:/";
    }
}
