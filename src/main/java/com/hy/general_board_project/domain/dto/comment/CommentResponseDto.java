package com.hy.general_board_project.domain.dto.comment;

import com.hy.general_board_project.domain.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String nickname;
    private Long boardId;
    private Long commentWriterId;
    private String commentWriterProfileImageStoreName;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
        this.nickname = comment.getUser() == null ? "탈퇴한 사용자" : comment.getUser().getNickname();
        this.boardId = comment.getBoard().getId();
        this.commentWriterId = comment.getUser() == null ? -1L : comment.getUser().getId();
        this.commentWriterProfileImageStoreName = null;

        if (comment.getUser() != null && comment.getUser().getProfileImage() != null) {
            this.commentWriterProfileImageStoreName = comment.getUser().getProfileImage().getStoreName();
        }
    }
}
