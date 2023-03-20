package com.hy.general_board_project.service;

import com.hy.general_board_project.web.dto.message.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Service
public class MessageService {

    // 사용자에게 팝업 메시지를 전달하고, 페이지를 리다이렉트 한다.
    public static String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
