package com.hy.general_board_project.domain.comment;

import com.hy.general_board_project.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(User user);
}
