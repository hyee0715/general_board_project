package com.hy.general_board_project.service;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import com.hy.general_board_project.web.dto.user.UserInfoUpdateRequestDto;
import com.hy.general_board_project.web.dto.user.UserPasswordUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingService {

    private static final int PAGE_NUMBER_COUNT_OF_ONE_BLOCK = 8; // 한 블럭에 존재하는 페이지 번호 개수
    private static final int POST_COUNT_OF_ONE_PAGE = 8; // 한 페이지에 존재하는 게시글 수

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final HttpSession httpSession;

    @Transactional
    public String updateUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUserForInfoUpdate(userInfoUpdateRequestDto);

        user.updateNickname(userInfoUpdateRequestDto.getNickname());

        return userInfoUpdateRequestDto.getNickname();
    }

    public User findUserForInfoUpdate(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        Optional<User> user = userRepository.findByEmailAndProvider(userInfoUpdateRequestDto.getEmail(), userInfoUpdateRequestDto.getProvider());

        if (userInfoUpdateRequestDto.getProvider().isEmpty()) {
            user = userRepository.findByUsername(userInfoUpdateRequestDto.getUsername());
        }

        return user.get();
    }

    public String findUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        return findUserForInfoUpdate(userInfoUpdateRequestDto).getNickname();
    }

    public UserInfoUpdateRequestDto findUserInfo() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            return new UserInfoUpdateRequestDto(user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider());
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        return new UserInfoUpdateRequestDto(user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider());
    }

    @Transactional
    public User updateUserPassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, String encodedNewPassword) {
        Optional<User> user = userRepository.findByUsername(userPasswordUpdateRequestDto.getUsername())
                .map(entity -> entity.updatePassword(encodedNewPassword));

        return userRepository.save(user.get());
    }
    
    public boolean isFormUser() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user == null) {
            return true;
        }

        return false;
    }

    @Transactional
    public List<BoardListResponseDto> getUserOwnBoardList(String writer, int pageNum) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<Board> userBoardList = boardRepository.findByWriter(writer, pageRequest);

        List<BoardListResponseDto> userBoardDtoList = new ArrayList<>();

        if (userBoardList.isEmpty()) return userBoardDtoList;

        userBoardList.stream()
                .map(BoardListResponseDto::convertBoardEntityToBoardListResponseDto)
                .forEach(userBoardDtoList::add);

        return userBoardDtoList;
    }

    @Transactional
    public Long getUserOwnBoardListCount(String writer) {
        List<Board> userBoardList = boardRepository.findByWriter(writer);

        return (long) userBoardList.size();
    }

    public Integer getTotalLastPageNum(String writer) {
        Double postsTotalCount = Double.valueOf(this.getUserOwnBoardListCount(writer));

        // 총 게시글의 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int) (Math.ceil((postsTotalCount / POST_COUNT_OF_ONE_PAGE)));

        return totalLastPageNum;
    }
}