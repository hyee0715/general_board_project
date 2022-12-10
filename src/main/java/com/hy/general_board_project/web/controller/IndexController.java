package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Controller
public class IndexController {

    private BoardService boardService;
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @GetMapping({"/", "/board/list"})
    public String index(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<BoardListResponseDto> boardList = boardService.getBoardList(pageNum);

        Integer totalLastPageNum = boardService.getTotalLastPageNum();

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("nickname", findUserNickname());
        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        return "index";
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
