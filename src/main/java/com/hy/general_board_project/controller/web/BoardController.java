package com.hy.general_board_project.controller.web;

import com.hy.general_board_project.domain.dto.comment.CommentResponseDto;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.service.SettingService;
import com.hy.general_board_project.domain.dto.board.BoardDetailResponseDto;
import com.hy.general_board_project.domain.dto.board.BoardSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;
    private final SettingService settingService;

    @GetMapping("/write")
    public String write(Model model) {
        String nickname = settingService.getUserNickname();

        model.addAttribute("nickname", nickname);
        model.addAttribute("view", 0);

        Optional<User> user = userRepository.findByNickname(nickname);

        if (user.isPresent()) {
            Long writerId = user.get().getId();
            model.addAttribute("writerId", writerId);
        }

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        return "board/write";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(id);
        List<CommentResponseDto> comments = boardDetailResponseDto.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
                boardService.updateView(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            boardService.updateView(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);

        String nickname = settingService.getUserNickname();
        model.addAttribute("nickname", nickname);

        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            Long userId = user.get().getId();
            model.addAttribute("userId", userId);
        }

        //현재 접속한 사용자의 프로필 사진 불러오기
        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        //각 게시물 글쓴이의 프로필 사진 불러오기
        Long writerId = boardDetailResponseDto.getWriterId();
        Optional<User> writerWrapper = userRepository.findById(writerId);

        if (writerWrapper.isPresent()) {
            String writerProfileImageStoreName = boardService.getWriterProfileImageStoreName(writerWrapper.get());

            if (writerProfileImageStoreName != null) {
                model.addAttribute("writerProfileImageStoreName", writerProfileImageStoreName);
            }

            return "board/detail";
        }

        model.addAttribute("writerProfileImageStoreName", null);

        return "board/detail";
    }

    @GetMapping("/detail/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.getBoardDetail(id);

        model.addAttribute("boardDetailResponseDto", boardDetailResponseDto);
        model.addAttribute("nickname", settingService.getUserNickname());

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        return "board/update";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model, @RequestParam(value="page", defaultValue = "1") int pageNum, @RequestParam(value="searchOption", required = false) String searchOption) {
        List<BoardSearchResponseDto> boardDtoList = boardService.search(keyword, pageNum, searchOption);

        int searchPostTotalCount = boardService.getSearchPostTotalCount(keyword, searchOption);
        int totalLastPageNum = boardService.getTotalLastSearchPageNum(searchPostTotalCount);
        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("nickname", settingService.getUserNickname());
        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);
        model.addAttribute("searchOption", searchOption);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        return "board/search";
    }
}
