package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.web.dto.message.MessageDto;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class EmailController {
    private final UserService userService;

    // 사용자에게 팝업 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

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
