package com.hy.general_board_project.web.dto.board;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public BoardUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
