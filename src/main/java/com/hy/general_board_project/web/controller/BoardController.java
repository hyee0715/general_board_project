package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.BoardSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {
    private BoardService boardService;

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @PostMapping("/write")
    public String write(BoardSaveRequestDto requestDto) {
        boardService.save(requestDto);
        return "redirect:/";
    }

    @GetMapping("/detail")
    public String detail() {
        return "board/detail";
    }

    @GetMapping("/update")
    public String update() {
        return "board/update";
    }
}
