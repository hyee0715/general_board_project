package com.hy.general_board_project.controller;

import com.hy.general_board_project.service.EmailService;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class EmailApiController {

    private final EmailService emailService;

    @PostMapping(value = "/user/email/send")
    public ResponseEntity sendMail(UserSignUpRequestDto userDto) throws MessagingException {
        String emailContent = emailService.makeEmailContentForJoin(userDto);

        emailService.sendMail(userDto.getEmail(), "[GENERAL BOARD 회원 가입 이메일 인증]", emailContent);
        return ResponseEntity.ok("회원 가입 인증 이메일 전송");
    }
}
