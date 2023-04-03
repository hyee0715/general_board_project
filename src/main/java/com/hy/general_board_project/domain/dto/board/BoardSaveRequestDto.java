package com.hy.general_board_project.domain.dto.board;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BoardSaveRequestDto {
    private String title;
    private String writer;
    private String content;
    private Integer view;
    private Long writerId;
    private User user;

    @Builder
    public BoardSaveRequestDto(String title, String content, String writer, Integer view, Long writerId, User user) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.view = view;
        this.writerId = writerId;
        this.user = user;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .view(view)
                .writerId(writerId)
                .user(user)
                .build();
    }
}
