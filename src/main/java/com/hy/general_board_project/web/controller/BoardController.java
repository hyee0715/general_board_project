package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {
    private BoardService boardService;
    private final HttpSession httpSession;

    @GetMapping("/write")
    public String write(Model model) {

        addSessionUserName(model);

        return "board/write";
    }

    @GetMapping("/detail/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        addSessionUserName(model);

        return "board/detail";
    }

    @GetMapping("/detail/update/{no}")
    public String update(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        addSessionUserName(model);

        return "board/update";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model, @RequestParam(value="page", defaultValue = "1") int pageNum, @RequestParam(value="searchOption", required = false) String searchOption) {
        List<BoardSearchResponseDto> boardDtoList = boardService.search(keyword, pageNum, searchOption);

        int searchPostTotalCount = boardService.getSearchPostTotalCount(keyword, searchOption);

        int totalLastPageNum = boardService.getTotalLastSearchPageNum(searchPostTotalCount);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        addSessionUserName(model);

        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);
        model.addAttribute("searchOption", searchOption);

        return "board/search";
    }

    public void addSessionUserName(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
    }
}
