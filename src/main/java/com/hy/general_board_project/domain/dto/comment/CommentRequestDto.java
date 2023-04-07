package com.hy.general_board_project.domain.dto.comment;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.comment.Comment;
import com.hy.general_board_project.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private User user;
    private Board board;

    public Comment toEntity() {
        Comment comment = Comment.builder()
                .id(id)
                .content(content)
                .user(user)
                .board(board)
                .build();

        return comment;
    }
}