package com.hy.general_board_project.web.dto;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDetailResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public BoardDetailResponseDto(Long id, String title, String writer, String content, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
    }

    // Entity -> BoardListResponseDto로 변환
    public static BoardDetailResponseDto convertBoardEntityToBoardDetailResponseDto(Board board) {
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
    }
}
