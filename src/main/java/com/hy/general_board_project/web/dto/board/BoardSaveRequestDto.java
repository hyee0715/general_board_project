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
    private Integer view;
    private Long writerId;

    @Builder
    public BoardSaveRequestDto(String title, String content, String writer, Integer view, Long writerId) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.view = view;
        this.writerId = writerId;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .view(view)
                .writerId(writerId)
                .build();
    }
}
