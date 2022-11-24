package com.hy.general_board_project.web.dto;

import com.hy.general_board_project.domain.board.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdDate;

    public BoardListResponseDto(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.writer = entity.getWriter();
        this.createdDate = entity.getCreatedDate();
    }
}
