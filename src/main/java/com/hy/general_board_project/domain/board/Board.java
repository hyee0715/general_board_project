package com.hy.general_board_project.domain.board;

import com.hy.general_board_project.domain.Time;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Board extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    private String profileImage;

    @Column(length = 10, nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer view;

    @Column
    private Long writerId;

    @Builder
    public Board(String title, String writer, String content, Integer view, Long writerId) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.view = view;
        this.writerId = writerId;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateWriter(String writer) {
        this.writer = writer;
    }
}
