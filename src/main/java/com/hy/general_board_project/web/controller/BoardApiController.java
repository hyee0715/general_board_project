package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.board.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/board/write")
    public Long write(@RequestBody BoardSaveRequestDto requestDto) {
        return boardService.save(requestDto);
    }

    @PutMapping("/board/detail/update/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {
        return boardService.update(id, boardUpdateRequestDto);
    }

    @DeleteMapping("/board/detail/{id}")
    public Long delete(@PathVariable Long id) {
        boardService.delete(id);
        return id;
    }
}
