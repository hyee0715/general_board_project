package com.hy.general_board_project.domain.comment;

import com.hy.general_board_project.domain.Time;
import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne // 한 개의 게시글에 여러 개의 댓글 작성 가능
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne // 한 명의 사용자가 여러 개의 댓글 작성 가능
    @JoinColumn(name = "user_id")
    private User user; // Comment writer

    public void update(String content) {
        this.content = content;
    }

    public void removeUser() {
        this.user = null;
    }
}