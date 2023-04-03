package com.hy.general_board_project.domain.dto.board;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.user.User;
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
    private String profileImage;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer view;
    private Long writerId;
    private User user;

    @Builder
    public BoardDetailResponseDto(Long id, String title, String writer, String profileImage, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, Integer view, Long writerId, User user) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.profileImage = profileImage;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.view = view;
        this.writerId = writerId;
        this.user = user;
    }

    // Entity -> BoardDetailResponseDto로 변환
    public static BoardDetailResponseDto convertBoardEntityToBoardDetailResponseDto(Board board) {
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .view(board.getView())
                .writerId(board.getWriterId())
                .user(board.getUser())
                .build();
    }
}
