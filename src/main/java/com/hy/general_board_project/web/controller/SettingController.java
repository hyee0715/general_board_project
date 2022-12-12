package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.SettingService;
import com.hy.general_board_project.web.dto.user.UserInfoUpdateRequestDto;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/setting/userInfo")
    public String userInfo(Model model) {
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = settingService.findUserInfo();

        model.addAttribute("userInfoUpdateRequestDto", userInfoUpdateRequestDto);

        return "setting/userInfo";
    }

    @PostMapping("/setting/userInfo")
    public String userInfoUpdate(@Validated @ModelAttribute UserInfoUpdateRequestDto userInfoUpdateRequestDto, BindingResult bindingResult) {

        if (!StringUtils.hasText(userInfoUpdateRequestDto.getNickname())) {
            bindingResult.rejectValue("nickname", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,10}$", userInfoUpdateRequestDto.getNickname())) {
                bindingResult.addError(new FieldError("userInfoUpdateRequestDto", "nickname", userInfoUpdateRequestDto.getNickname(), false, null, null, "닉네임은 특수문자 미포함 2~10자리 제한입니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors ={}", bindingResult);
            return "/setting/userInfo";
        }

        settingService.updateUserNickname(userInfoUpdateRequestDto);

        return "redirect:/setting/userInfo";
    }
}
