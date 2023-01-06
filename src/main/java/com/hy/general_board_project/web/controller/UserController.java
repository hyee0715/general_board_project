package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.validator.CheckEmailAndProviderValidator;
import com.hy.general_board_project.validator.CheckNicknameValidator;
import com.hy.general_board_project.validator.CheckUsernameValidator;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailAndProviderValidator checkEmailAndProviderValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkNicknameValidator);
        binder.addValidators(checkEmailAndProviderValidator);
    }

    @GetMapping("/user/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        HttpServletRequest request, Model model) {

        /**
         * 이전 페이지로 되돌아가기 위한 Referer 헤더값을 세션의 prevPage attribute로 저장
         */
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/user")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

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

        userSignUpRequestDto.setCertified(certifiedKey());

        userService.joinUser(userSignUpRequestDto);
        return "redirect:/user/login";
    }

    private String certifiedKey() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = random.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < 10);
        return sb.toString();
    }

    @GetMapping("/user/emailCertified")
    public String certifyEmail(Model model) {
        UserSignUpRequestDto emailAuthInfo = userService.getEmailCertifiedInfo();
        model.addAttribute("userSignUpRequestDto", emailAuthInfo);

        return "account/emailCertified";
    }

    @GetMapping("/user/findUsername")
    public String findUsername() {
        return "account/findUsername";
    }
}
