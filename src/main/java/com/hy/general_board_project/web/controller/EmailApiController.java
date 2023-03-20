package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.EmailService;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@RestController
public class EmailApiController {

    private final EmailService emailService;

    @GetMapping(value = "/user/email/send")
    public void sendMail(UserSignUpRequestDto userDto) throws MessagingException {
        String emailContent = emailService.makeEmailContentForJoin(userDto);

        emailService.sendMail(userDto.getEmail(), "[GENERAL BOARD 회원 가입 이메일 인증]", emailContent);
    }
}
