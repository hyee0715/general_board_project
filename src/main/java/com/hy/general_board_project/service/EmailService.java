package com.hy.general_board_project.service;

import com.hy.general_board_project.web.dto.user.FindPasswordDto;
import com.hy.general_board_project.web.dto.user.FindUsernameDto;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("GENERAL_BOARD"); //보내는 사람
        helper.setTo(toEmail); //받는 사람
        helper.setSubject(subject); //메일 제목
        helper.setText(message, true); //true를 넣을경우 html 전송

        javaMailSender.send(mimeMessage);
    }

    public String makeEmailContentForUsername(FindUsernameDto userDto) {
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
                        "		안녕하세요.<br />" +
                        "		GENERAL BOARD 입니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 본인 인증을 완료해 주세요.<br />" +
                        "		본인 인증 후 아이디를 확인하실 수 있습니다.<br />" +
                        "		감사합니다." +
                        "	</p>" +
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                        "	href=\"http://ec2-43-201-158-17.ap-northeast-2.compute.amazonaws.com:8080/user/findUsername/email/certified?email=" + userDto.getEmail() + "&realName=" + userDto.getRealName() + "&username=" + userDto.getUsername() + "\" target=\"_blank\">" +
                        "		<p" +
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                        "			메일 인증</p>" +
                        "	</a>" +
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>"
        );
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();
    }

    public String makeEmailContentForPassword(FindPasswordDto userDto) {
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
                        "		<span style=\"color: #02b875\">임시 비밀번호</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        userDto.getUsername() + " 님 안녕하세요.<br />" +
                        "		GENERAL BOARD 입니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'임시 비밀번호'</b> 를 확인해주세요.<br />" +
                        "		임시 비밀번호를 통해 로그인 후, 보안을 위해 즉시 비밀번호를 변경해주세요.<br />" +
                        "		감사합니다." +
                        "	</p>" +

                        "	<p style=\"font-size: 20px; line-height: 26px; margin-top: 50px; text-align: center; font-weight: bold;\">" +
                        userDto.getTempPassword() +
                        "	</p>" +
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                        "	href=\"http://ec2-43-201-158-17.ap-northeast-2.compute.amazonaws.com:8080/user/login" + "\" target=\"_blank\">" +
                        "		<p" +
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                        "			로그인 하기</p>" +
                        "	</a>" +
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>"
        );
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();
    }

    public String makeEmailContentForJoin(UserSignUpRequestDto userDto) {
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
                        userDto.getUsername() +
                        "		님 안녕하세요.<br />" +
                        "		GENERAL BOARD에 가입해 주셔서 진심으로 감사드립니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br /> " +
                        "		감사합니다." +
                        "	</p>" +
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\"" +
                        "	href=\"http://ec2-43-201-158-17.ap-northeast-2.compute.amazonaws.com:8080/user/email/certified?email=" + userDto.getEmail() + "&certified=" + userDto.getCertified() + "\" target=\"_blank\">" +
                        "		<p" +
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">" +
                        "			메일 인증</p>" +
                        "	</a>" +
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>"
        );
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();
    }
}