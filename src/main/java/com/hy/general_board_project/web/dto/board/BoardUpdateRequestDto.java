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

//    public Board toEntity() {
//        return Board.builder()
//                .title(title)
//                .content(content)
//                .build();
//    }

    // Entity -> BoardUpdateResponseDto로 변환
//    public static BoardUpdateRequestDto convertBoardEntityToBoardUpdateResponseDto(Board board) {
//        return BoardUpdateRequestDto.builder()
//                .title(board.getTitle())
//                .writer(board.getWriter())
//                .content(board.getContent())
//                .modifiedDate(board.getModifiedDate())
//                .build();
//    }
}
