package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/board/write")
    public Long write(@RequestBody BoardSaveRequestDto requestDto) {
        return boardService.save(requestDto);
    }
}
