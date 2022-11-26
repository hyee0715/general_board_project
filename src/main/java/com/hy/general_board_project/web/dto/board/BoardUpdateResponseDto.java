package com.hy.general_board_project.web.dto.board;

import com.hy.general_board_project.domain.board.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardUpdateResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardUpdateResponseDto(Long id, String title, String writer, String content, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.modifiedDate = modifiedDate;
    }

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }

    // Entity -> BoardUpdateResponseDto로 변환
    public static BoardUpdateResponseDto convertBoardEntityToBoardUpdateResponseDto(Board board) {
        return BoardUpdateResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
