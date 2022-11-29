package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {
    private BoardService boardService;
    private final HttpSession httpSession;

    @GetMapping({"/", "/board/list"})
    public String index(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<BoardListResponseDto> boardList = boardService.getBoardList(pageNum);

        Integer totalLastPageNum = boardService.getTotalLastPageNum();

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        return "index";
    }
}
