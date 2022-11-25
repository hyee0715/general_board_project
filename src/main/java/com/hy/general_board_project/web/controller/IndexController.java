package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.BoardListResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {

    private BoardService boardService;

    @GetMapping("/")
    public String index(Model model) {
        List<BoardListResponseDto> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "index";
    }


}
