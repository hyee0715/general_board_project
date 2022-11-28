package com.hy.general_board_project.web.dto.board;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardSaveRequestDto {
    private String title;
    private String writer;
    private String content;

    @Builder
    public BoardSaveRequestDto(String title, String content, String writer) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }
}
