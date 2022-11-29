package com.hy.general_board_project.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class AccountController {

    @GetMapping("/user/login")
    public String account() {
        return "account/login";
    }

    @GetMapping("/user/signUp")
    public String signUp() {
        return "account/signUp";
    }
}
