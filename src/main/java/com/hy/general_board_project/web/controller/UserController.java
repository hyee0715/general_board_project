package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.web.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {

    private UserService userService;

    @GetMapping("/user/login")
    public String account() {
        return "account/login";
    }

    @GetMapping("/user/signUp")
    public String signUp(Model model) {
        model.addAttribute("userDto", new UserDto());

        return "account/signUp";
    }
}
