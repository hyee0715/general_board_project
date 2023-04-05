package com.hy.general_board_project.controller.web;

import static com.hy.general_board_project.service.MessageService.showMessageAndRedirect;

import com.hy.general_board_project.service.EmailService;
import com.hy.general_board_project.service.UserFindService;
import com.hy.general_board_project.domain.dto.message.MessageDto;
import com.hy.general_board_project.domain.dto.user.FindPasswordDto;
import com.hy.general_board_project.domain.dto.user.FindUsernameDto;
import com.hy.general_board_project.validator.validation.user.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;

@Slf4j
@AllArgsConstructor
@Controller
public class UserFindController {

    private final UserFindService userFindService;
    private final EmailService emailService;

    @GetMapping("/user/findUsername")
    public String findUsername(Model model) {
        model.addAttribute("findUsernameDto", new FindUsernameDto());
        return "account/findUsername";
    }

    @PostMapping("/user/findUsername")
    public String findUsername(@Validated({RealNameValidationSequence.class, EmailValidationSequence.class, FindUsernameValidationSequence.class}) @ModelAttribute FindUsernameDto findUsernameDto, BindingResult bindingResult, Model model) throws MessagingException {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "account/findUsername";
        }

        FindUsernameDto userDto = userFindService.getFindUsernameDtoByEmail(findUsernameDto.getEmail());
        String emailContent = emailService.makeEmailContentForUsername(userDto);
        emailService.sendMail(userDto.getEmail(), "[GENERAL BOARD 아이디 찾기 이메일 인증]", emailContent);

        MessageDto message = new MessageDto("이메일이 전송되었습니다.", "/user/findUsername", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping(value = "/user/findUsername/email/certified")
    public String getFindUsernameResult(FindUsernameDto findUsernameDto, Model model) {
        log.info("username = {}", findUsernameDto.getUsername());
        log.info("realName = {}", findUsernameDto.getRealName());
        log.info("email = {}", findUsernameDto.getEmail());

        model.addAttribute("findUsernameDto", findUsernameDto);

        return "account/findUsernameResult";
    }

    @GetMapping("/user/findPassword")
    public String findPassword(Model model) {
        model.addAttribute("findPasswordDto", new FindPasswordDto());
        return "account/findPassword";
    }

    @PostMapping("/user/findPassword")
    public String findPassword(@Validated({RealNameValidationSequence.class, UsernameValidationSequence.class, EmailValidationSequence.class, FindPasswordValidationSequence.class})  @ModelAttribute FindPasswordDto findPasswordDto, BindingResult bindingResult, Model model) throws MessagingException {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "account/findPassword";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String tempPassword = userFindService.makeTempPassword();
        findPasswordDto.setTempPassword(tempPassword);
        String encodedTempPassword = encoder.encode(findPasswordDto.getTempPassword());
        userFindService.updateUserPasswordToTempPassword(findPasswordDto, encodedTempPassword);

        String emailContent = emailService.makeEmailContentForPassword(findPasswordDto);
        emailService.sendMail(findPasswordDto.getEmail(), "[GENERAL BOARD 임시 비밀번호 안내]", emailContent);

        MessageDto message = new MessageDto("임시 비밀번호가 이메일로 전송되었습니다.", "/user/findPassword", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
