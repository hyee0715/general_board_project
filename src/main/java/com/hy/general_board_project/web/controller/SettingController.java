package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.service.SettingService;
import com.hy.general_board_project.validator.CheckNicknameValidator;
import com.hy.general_board_project.web.dto.message.MessageDto;
import com.hy.general_board_project.web.dto.user.UserInfoUpdateRequestDto;
import com.hy.general_board_project.web.dto.user.UserPasswordUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SettingController {

    private final SettingService settingService;
    private final CheckNicknameValidator checkNicknameValidator;
    private final BoardService boardService;
    private final UserRepository userRepository;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkNicknameValidator);
    }

    @GetMapping("/setting/userInfo")
    public String moveToUserInfo(Model model) {
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = settingService.findUserInfo();
        model.addAttribute("userInfoUpdateRequestDto", userInfoUpdateRequestDto);

        return "setting/userInfo";
    }

    @PostMapping("/setting/userInfo")
    public String UpdateUserInfo(@Validated @ModelAttribute UserInfoUpdateRequestDto userInfoUpdateRequestDto, BindingResult bindingResult, Model model) {

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

        String originalUserNickname = settingService.findUserNickname(userInfoUpdateRequestDto);

        String newUserNickname = settingService.updateUserNickname(userInfoUpdateRequestDto);

        //해당 사용자의 모든 게시물 작성자 이름 수정
        boardService.updateBoardWriter(originalUserNickname, newUserNickname);

        MessageDto message = new MessageDto("회원 정보 수정이 완료되었습니다.", "/setting/userInfo", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    @GetMapping("/setting/userList")
    public String moveToUserList() {
        return "/setting/userList";
    }

    @GetMapping("/setting/userPassword")
    public String moveToUserPassword(Model model) {
        UserInfoUpdateRequestDto userInfoUpdateRequestDto = settingService.findUserInfo();
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = userInfoUpdateRequestDto.convertToPasswordUpdateDto();

        model.addAttribute("userPasswordUpdateRequestDto", userPasswordUpdateRequestDto);
        log.info("userPasswordUpdateRequestDto.username = {}", userPasswordUpdateRequestDto.getUsername());

        return "/setting/userPassword";
    }

    @PostMapping("/setting/userPassword")
    public String updateUserPassword(@Validated @ModelAttribute UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, BindingResult bindingResult) {

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getNewPassword())) {
            bindingResult.rejectValue("password", "required", "");
        } else {
            if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", userPasswordUpdateRequestDto.getNewPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPassword", userPasswordUpdateRequestDto.getNewPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "setting/userPassword";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedNewPassword = encoder.encode(userPasswordUpdateRequestDto.getNewPassword());

        settingService.updateUserPassword(userPasswordUpdateRequestDto, encodedNewPassword);

        return "redirect:/setting/userPassword";
    }
}
