package com.hy.general_board_project.controller.web;

import static com.hy.general_board_project.service.MessageService.showMessageAndRedirect;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.domain.dto.message.MessageDto;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class EmailController {
    private final UserService userService;

    @GetMapping(value = "/user/email/certified")
    public String checkMail(HttpServletRequest request, UserSignUpRequestDto userSignUpRequestDto, Model model) throws MessagingException {
        HttpSession session = request.getSession();
        User user = userService.emailCertifiedCheck(userSignUpRequestDto);

        if(user != null) {
            userService.emailCertifiedUpdate(userSignUpRequestDto);
            SecurityContextHolder.getContext().setAuthentication(null);
            session.removeAttribute("Authorization");
        }

        MessageDto message = new MessageDto("이메일 인증이 완료되었습니다.", "/", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
