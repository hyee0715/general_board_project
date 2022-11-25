package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.BoardUpdateResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/detail/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        return "board/detail";
    }

    @GetMapping("/detail/update/{no}")
    public String update(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        return "board/update";
    }

    @PutMapping("/detail/update/{no}")
    public String update(BoardUpdateResponseDto boardUpdateResponseDto) {
        boardService.update(boardUpdateResponseDto);

        return "redirect:/";
    }
}
