package com.hy.general_board_project.controller.web;

import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.service.ValidationService;
import com.hy.general_board_project.validator.CheckEmailAndProviderValidator;
import com.hy.general_board_project.validator.CheckNicknameValidator;
import com.hy.general_board_project.validator.CheckUsernameValidator;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckNicknameValidator checkNicknameValidator;
    private final CheckEmailAndProviderValidator checkEmailAndProviderValidator;
    private final ValidationService validationService;

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

        bindingResult = validationService.validateRealNameForSignUp(userSignUpRequestDto, bindingResult);
        bindingResult = validationService.validateUsernameForSignUp(userSignUpRequestDto, bindingResult);
        bindingResult = validationService.validateNicknameForSignUp(userSignUpRequestDto, bindingResult);
        bindingResult = validationService.validatePasswordForSignUp(userSignUpRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "account/signUp";
        }

        userSignUpRequestDto.setCertified(userService.makeCertifiedKey());

        userService.joinUser(userSignUpRequestDto);
        return "redirect:/user/login";
    }

    @GetMapping("/user/emailCertified")
    public String certifyEmail(Model model) {
        UserSignUpRequestDto emailAuthInfo = userService.getEmailCertifiedInfo();
        model.addAttribute("userSignUpRequestDto", emailAuthInfo);

        return "account/emailCertified";
    }
}
