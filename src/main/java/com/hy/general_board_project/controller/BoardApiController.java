package com.hy.general_board_project.controller;

import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.domain.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.domain.dto.board.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/board/write")
    public ResponseEntity write(@RequestBody BoardSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.save(requestDto));
    }

    @PutMapping("/board/detail/update/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.update(id, boardUpdateRequestDto));
    }

    @DeleteMapping("/board/detail/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
