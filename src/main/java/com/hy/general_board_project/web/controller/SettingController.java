package com.hy.general_board_project.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingController {

    @GetMapping("/setting/userInfo")
    public String userInfo() {
        return "setting/userInfo";
    }

}
