package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.PrincipalDetailsService;
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
    private final PrincipalDetailsService principalDetailsService;

    @GetMapping("/user/login")
    public String account() {
        return "account/login";
    }

    @GetMapping("/user/signUp")
    public String signUp() {
        return "account/signUp";
    }

    @PostMapping("/user/signUp")
    public String signUp(@ModelAttribute User user) {
        principalDetailsService.joinUser(user);

        return "redirect:/";
    }
}
