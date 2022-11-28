package com.hy.general_board_project.service;

import com.hy.general_board_project.web.dto.board.BoardUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Test
    void update() {
        Long id = 201L;
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title("제목 수정")
                .content("내용 수정")
                .build();

        Long result = boardService.update(id, requestDto);

        Assertions.assertThat(result).isEqualTo(id);

    }
}