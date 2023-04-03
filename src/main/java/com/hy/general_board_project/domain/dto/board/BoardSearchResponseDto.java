package com.hy.general_board_project.domain.dto.board;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSearchResponseDto {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdDate;
    private Long writerId;
    private Integer view;

    @Builder
    public BoardSearchResponseDto(Long id, String title, String writer, LocalDateTime createdDate, Long writerId, Integer view) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdDate = createdDate;
        this.writerId = writerId;
        this.view = view;
    }

    // Entity -> BoardSearchResponseDto로 변환
    public static BoardSearchResponseDto convertBoardEntityToBoardSearchResponseDto(Board board) {
        return BoardSearchResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .writerId(board.getWriterId())
                .view(board.getView())
                .build();
    }
}
