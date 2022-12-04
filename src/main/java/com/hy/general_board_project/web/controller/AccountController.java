package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class AccountController {

    private final UserRepository userRepository;

    @GetMapping("/user/login")
    public String account() {
        return "account/login";
    }
}
