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
    public void sendMail(UserSignUpRequestDto user) throws MessagingException {
        StringBuffer emailContent = new StringBuffer();
        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html>");
        emailContent.append("<head>");
        emailContent.append("</head>");
        emailContent.append("<body>");
        emailContent.append(
                " <div" +
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">GENERAL BOARD</span><br />" +
                        "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        user.getUsername() +
                        "		님 안녕하세요.<br />" +
                        "		GENERAL BOARD에 가입해 주셔서 진심으로 감사드립니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br /> " +
                        "		감사합니다." +
                        "	</p>" +
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                        "	href=\"http://localhost:8080/user/email/certified?email=" + user.getEmail() + "&certified=" + user.getCertified() + "\" target=\"_blank\">" +
                        "		<p" +
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                        "			메일 인증</p>" +
                        "	</a>" +
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>"
        );
        emailContent.append("</body>");
        emailContent.append("</html>");
        emailService.sendMail(user.getEmail(), "[GENERAL BOARD 회원 가입 이메일 인증]", emailContent.toString());
    }
}
