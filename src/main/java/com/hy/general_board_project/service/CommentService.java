package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.comment.Comment;
import com.hy.general_board_project.domain.comment.CommentRepository;
import com.hy.general_board_project.domain.dto.comment.CommentRequestDto;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long id, CommentRequestDto dto, User user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패, 해당 게시글이 존재하지 않습니다." + id));

        dto.setUser(user);
        dto.setBoard(board);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    public Long update(Long id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + id));

        comment.update(dto.getContent());
        return comment.getId();
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. " + id));

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteUserRelation(Long userId) {
        User user = userRepository.findById(userId).get();
        List<Comment> comments = commentRepository.findByUser(user);

        for (Comment comment : comments) {
            comment.removeUser();
        }
    }
}
