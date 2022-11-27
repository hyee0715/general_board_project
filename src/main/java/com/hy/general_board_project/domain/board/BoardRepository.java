package com.hy.general_board_project.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword);
    List<Board> findByContentContaining(String keyword);
    List<Board> findByWriterContaining(String keyword);

    @Query("SELECT m FROM Board m WHERE m.title LIKE %:keyword% or m.content LIKE %:keyword% or m.writer LIKE %:keyword%")
    List<Board> findByAllOptionContaining(@Param("keyword") String keyword);
}
