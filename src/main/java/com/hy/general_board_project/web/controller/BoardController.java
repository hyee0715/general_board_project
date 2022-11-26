package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.BoardUpdateResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/detail/{no}")
    public String delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return "redirect:/";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<BoardSearchResponseDto> boardSearchDtoList = boardService.search(keyword);

        model.addAttribute("boardList", boardSearchDtoList);
        model.addAttribute("keyword", keyword);

        return "board/search";
    }
}
