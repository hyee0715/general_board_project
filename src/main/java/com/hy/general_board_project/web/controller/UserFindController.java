package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.EmailService;
import com.hy.general_board_project.service.UserFindService;
import com.hy.general_board_project.web.dto.message.MessageDto;
import com.hy.general_board_project.web.dto.user.FindUsernameDto;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.util.regex.Pattern;

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
    public String findUsername(@Validated @ModelAttribute FindUsernameDto findUsernameDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws MessagingException {
        boolean globalErrorCheck = true;

        if (!StringUtils.hasText(findUsernameDto.getRealName())) {
            bindingResult.rejectValue("realName", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣]{2,6}$", findUsernameDto.getRealName())) {
                bindingResult.addError(new FieldError("findUsernameDto", "realName", findUsernameDto.getRealName(), false, null, null, "이름은 한글로 구성된 2~6자리 제한입니다."));
            }
        }

        if (!StringUtils.hasText(findUsernameDto.getEmail())) {
            bindingResult.rejectValue("email", "required", "");
        } else {
            if (!Pattern.matches("^(.+)@(.+)$", findUsernameDto.getEmail())) {
                bindingResult.addError(new FieldError("findUsernameDto", "email", findUsernameDto.getEmail(), false, null, null, "이메일 형식에 맞지 않습니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            globalErrorCheck = false;
        }

        if (globalErrorCheck && !userFindService.existsUserByRealNameAndEmail(findUsernameDto.getRealName(), findUsernameDto.getEmail())) {
            bindingResult.reject("noUser", "입력하신 정보와 일치하는 계정이 존재하지 않습니다.");
        }

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
    public String getFindUsernameResult(FindUsernameDto findUsernameDto, Model model) throws MessagingException {
        log.info("username = {}", findUsernameDto.getUsername());
        log.info("realName = {}", findUsernameDto.getRealName());
        log.info("email = {}", findUsernameDto.getEmail());

        model.addAttribute("findUsernameDto", findUsernameDto);

        return "account/findUsernameResult";
    }

    // 사용자에게 팝업 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
