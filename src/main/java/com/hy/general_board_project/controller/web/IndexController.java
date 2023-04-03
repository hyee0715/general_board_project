package com.hy.general_board_project.controller.web;

import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.service.SettingService;
import com.hy.general_board_project.domain.dto.board.BoardListResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {

    private BoardService boardService;
    private final SettingService settingService;

    @GetMapping({"/", "/board/list"})
    public String index(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<BoardListResponseDto> boardList = boardService.getBoardList(pageNum);
        int totalLastPageNum = boardService.getTotalLastPageNum();
        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("nickname", settingService.getUserNickname());
        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        return "index";
    }
}
