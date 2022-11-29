package com.hy.general_board_project.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class AccountController {

    @GetMapping("/login")
    public String account() {
        return "account/login";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "account/signUp";
    }

    @GetMapping("/signUp2")
    public String signUp2() {
        return "account/ddd";
    }

}
