package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @GetMapping("/write")
    public String write(Model model) {

        model.addAttribute("nickname", findUserNickname());

        return "board/write";
    }

    @GetMapping("/detail/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("nickname", findUserNickname());

        log.info("boardDetailResponseDto.writer = {}" , boardDetailResponseDto.getWriter());
        log.info("nickname = {}" , findUserNickname());

        return "board/detail";
    }

    @GetMapping("/detail/update/{no}")
    public String update(@PathVariable("no") Long no, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(no);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("nickname", findUserNickname());

        return "board/update";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model, @RequestParam(value="page", defaultValue = "1") int pageNum, @RequestParam(value="searchOption", required = false) String searchOption) {
        List<BoardSearchResponseDto> boardDtoList = boardService.search(keyword, pageNum, searchOption);

        int searchPostTotalCount = boardService.getSearchPostTotalCount(keyword, searchOption);

        int totalLastPageNum = boardService.getTotalLastSearchPageNum(searchPostTotalCount);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("nickname", findUserNickname());
        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);
        model.addAttribute("searchOption", searchOption);

        return "board/search";
    }

    public String findUserNickname() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> user = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            return user.get().getNickname();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);

        return user.get().getNickname();
    }
}
