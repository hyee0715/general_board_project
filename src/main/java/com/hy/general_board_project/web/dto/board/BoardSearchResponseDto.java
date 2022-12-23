package com.hy.general_board_project.web.dto.board;

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
    private LocalDateTime modifiedDate;
    private Long writerId;

    @Builder
    public BoardSearchResponseDto(Long id, String title, String writer, LocalDateTime createdDate, LocalDateTime modifiedDate, Long writerId) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.writerId = writerId;
    }

    // Entity -> BoardSearchResponseDto로 변환
    public static BoardSearchResponseDto convertBoardEntityToBoardSearchResponseDto(Board board) {
        return BoardSearchResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .writerId(board.getWriterId())
                .build();
    }
}
