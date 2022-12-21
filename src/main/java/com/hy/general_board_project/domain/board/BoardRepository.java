package com.hy.general_board_project.domain.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword);
    List<Board> findByTitleContaining(String keyword, Pageable pageable);

    List<Board> findByContentContaining(String keyword);
    List<Board> findByContentContaining(String keyword, Pageable pageable);

    List<Board> findByWriterContaining(String keyword);
    List<Board> findByWriterContaining(String keyword, Pageable pageable);

    @Query("SELECT m FROM Board m WHERE m.title LIKE %:keyword% or m.content LIKE %:keyword% or m.writer LIKE %:keyword%")
    List<Board> findByAllOptionContaining(@Param("keyword") String keyword);

    @Query("SELECT m FROM Board m WHERE m.title LIKE %:keyword% or m.content LIKE %:keyword% or m.writer LIKE %:keyword%")
    List<Board> findByAllOptionContaining(@Param("keyword") String keyword, Pageable pageable);

    List<Board> findByWriter(String writer);
    List<Board> findByWriter(String writer, Pageable pageable);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and m.title LIKE %:keyword%")
    List<Board> findByWriterAndTitleOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and m.title LIKE %:keyword%")
    List<Board> findByWriterAndTitleOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and m.content LIKE %:keyword%")
    List<Board> findByWriterAndContentOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and m.content LIKE %:keyword%")
    List<Board> findByWriterAndContentOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and (m.title LIKE %:keyword% or m.content LIKE %:keyword%)")
    List<Board> findByWriterAndTitleOptionOrContentOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword);

    @Query("SELECT m FROM Board m WHERE m.writer = :writer and (m.title LIKE %:keyword% or m.content LIKE %:keyword%)")
    List<Board> findByWriterAndTitleOptionOrContentOptionContaining(@Param("writer") String writer, @Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("update Board q set q.view = q.view + 1 where q.id = :id")
    Integer updateView(@Param("id") Long id);
}
