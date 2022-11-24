package com.hy.general_board_project.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @GetMapping("/detail")
    public String detail() {
        return "board/detail";
    }
}
