package com.hy.general_board_project.controller;

import com.hy.general_board_project.domain.dto.comment.CommentRequestDto;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.service.CommentService;
import com.hy.general_board_project.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;
    private final SettingService settingService;

    @PostMapping("/board/detail/{boardId}/comments")
    public ResponseEntity save(@PathVariable Long boardId, @RequestBody CommentRequestDto dto) {
        User user = settingService.getCurrentUser();
        return ResponseEntity.ok(commentService.save(boardId, dto, user));
    }

    @PutMapping("/board/detail/{boardId}/comments/{commentId}")
    public ResponseEntity update(@PathVariable Long boardId, @RequestBody CommentRequestDto dto, @PathVariable Long commentId) {
        commentService.update(commentId, dto);
        return ResponseEntity.ok(commentId);
    }

    @DeleteMapping("/board/detail/{boardId}/comments/{commentId}")
    public ResponseEntity delete(@PathVariable Long boardId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok(commentId);
    }
}
