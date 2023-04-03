package com.hy.general_board_project.domain.dto.board;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardListResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdDate;
    private Integer view;

    @Builder
    public BoardListResponseDto(Long id, String title, String writer, LocalDateTime createdDate,  Integer view) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdDate = createdDate;
        this.view = view;
    }

    // Entity -> BoardListResponseDto로 변환
    public static BoardListResponseDto convertBoardEntityToBoardListResponseDto(Board board) {
        return BoardListResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .view(board.getView())
                .build();
    }
}
