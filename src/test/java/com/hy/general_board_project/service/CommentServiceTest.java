package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.comment.Comment;
import com.hy.general_board_project.domain.comment.CommentRepository;
import com.hy.general_board_project.domain.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.domain.dto.comment.CommentRequestDto;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    User user;
    Long boardId;
    CommentRequestDto commentRequestDto;

    @BeforeEach
    public void signUp() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("abcde")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aa@aa")
                        .certified("Y")
                        .build();

        Long userId = userService.joinUser(requestDto);
        user = userRepository.findById(userId).get();

        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer(user.getNickname())
                .content("작성 내용")
                .view(0)
                .writerId(user.getId())
                .user(user)
                .build();

        boardId = boardService.save(boardSaveRequestDto);

        Board board = boardRepository.findById(boardId).get();

        commentRequestDto = CommentRequestDto.builder()
                .content("댓글 작성111")
                .user(user)
                .board(board)
                .build();
    }

    @Test
    @DisplayName("댓글 저장하기")
    @Transactional
    void save() {
        Long commentId = commentService.save(boardId, commentRequestDto, user);

        Comment comment = commentRepository.findById(commentId).get();

        Assertions.assertThat(comment.getContent()).isEqualTo("댓글 작성111");
    }

    @Test
    @DisplayName("댓글 저장하기 실패 (게시물이 존재하지 않음)")
    @Transactional
    void saveFailed() {
        assertThatThrownBy(() -> commentService.save(1000000000L, commentRequestDto, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("댓글 쓰기 실패, 해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글 수정하기")
    @Transactional
    void update() {
        Board board = boardRepository.findById(boardId).get();

        Long commentId = commentService.save(boardId, commentRequestDto, user);

        CommentRequestDto commentUpdateDto = CommentRequestDto.builder()
                .content("댓글 수정")
                .user(user)
                .board(board)
                .build();

        Long updatedCommentId = commentService.update(commentId, commentUpdateDto);

        Assertions.assertThat(commentId).isEqualTo(updatedCommentId);
    }

    @Test
    @DisplayName("댓글 수정하기 실패")
    @Transactional
    void updateFailed() {
        Board board = boardRepository.findById(boardId).get();

        CommentRequestDto commentUpdateDto = CommentRequestDto.builder()
                .content("댓글 수정")
                .user(user)
                .board(board)
                .build();

        assertThatThrownBy(() -> commentService.update(1000000000L, commentUpdateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 댓글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글 삭제하기")
    @Transactional
    void delete() {
        Long commentId = commentService.save(boardId, commentRequestDto, user);

        commentService.delete(commentId);

        Optional<Comment> comment = commentRepository.findById(commentId);

        Assertions.assertThat(comment).isEmpty();
    }

    @Test
    @DisplayName("댓글 삭제하기 실패")
    @Transactional
    void deleteFailed() {

        assertThatThrownBy(() -> commentService.delete(10000000000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 댓글이 존재하지 않습니다.");
    }
}