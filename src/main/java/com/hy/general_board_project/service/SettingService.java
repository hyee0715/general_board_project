package com.hy.general_board_project.service;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.profileImage.ProfileImageRepository;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.user.FormUserWithdrawRequestDto;
import com.hy.general_board_project.web.dto.user.SocialUserWithdrawRequestDto;
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

    private static final int POST_COUNT_OF_ONE_PAGE = 10; // 한 페이지에 존재하는 게시글 수

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ProfileImageRepository profileImageRepository;
    private final HttpSession httpSession;

    @Transactional
    public String updateUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUserForInfoUpdate(userInfoUpdateRequestDto);

        user.updateNickname(userInfoUpdateRequestDto.getNickname());

        return userInfoUpdateRequestDto.getNickname();
    }

    @Transactional
    public void updateUserProfileImage(UserInfoUpdateRequestDto userInfoUpdateRequestDto, ProfileImage profileImage) {
        User user = findUserForInfoUpdate(userInfoUpdateRequestDto);

        user.updateProfileImage(profileImage);
    }

    public User findUserForInfoUpdate(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        Optional<User> user = userRepository.findByEmailAndProvider(userInfoUpdateRequestDto.getEmail(), userInfoUpdateRequestDto.getProvider());

        if (userInfoUpdateRequestDto.getProvider().isEmpty()) {
            user = userRepository.findByUsername(userInfoUpdateRequestDto.getUsername());
        }

        return user.get();
    }

    public UserInfoUpdateRequestDto findUserInfo() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            if (user.getProfileImage() != null) {
                return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), user.getProfileImage().getId());
            }

            return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), null);
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        if (user.getProfileImage() != null) {
            return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), user.getProfileImage().getId());
        }

        return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), null);
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
    public List<BoardListResponseDto> getUserOwnBoardList(Long writerId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<Board> userBoardList = boardRepository.findByWriterId(writerId, pageRequest);

        List<BoardListResponseDto> userBoardDtoList = new ArrayList<>();

        if (userBoardList.isEmpty()) return userBoardDtoList;

        userBoardList.stream()
                .map(BoardListResponseDto::convertBoardEntityToBoardListResponseDto)
                .forEach(userBoardDtoList::add);

        return userBoardDtoList;
    }

    @Transactional
    public Long getUserOwnBoardListCount(Long writerId) {
        List<Board> userBoardList = boardRepository.findByWriterId(writerId);

        return (long) userBoardList.size();
    }

    public Integer getTotalLastPageNum(Long writerId) {
        Double postsTotalCount = Double.valueOf(this.getUserOwnBoardListCount(writerId));

        // 총 게시글의 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int) (Math.ceil((postsTotalCount / POST_COUNT_OF_ONE_PAGE)));

        return totalLastPageNum;
    }

    @Transactional
    public List<BoardSearchResponseDto> search(Long writerId, String keyword, int pageNum, String searchOption) {
        List<Board> boardSearchList = makeUserOwnBoardSearchList(writerId, keyword, pageNum, searchOption);

        List<BoardSearchResponseDto> boardSearchDtoList = new ArrayList<>();

        if (boardSearchList.isEmpty())
            return boardSearchDtoList;

        boardSearchList.stream()
                .map(BoardSearchResponseDto::convertBoardEntityToBoardSearchResponseDto)
                .forEach(boardSearchDtoList::add);

        return boardSearchDtoList;
    }

    @Transactional
    public List<Board> makeUserOwnBoardSearchList(Long writerId, String keyword, int pageNum, String searchOption) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (searchOption.equals("title")) {
            return boardRepository.findByWriterIdAndTitleOptionContaining(writerId, keyword, pageRequest);
        } else if (searchOption.equals("content")) {
            return boardRepository.findByWriterIdAndContentOptionContaining(writerId, keyword, pageRequest);
        }

        return boardRepository.findByWriterIdAndTitleOptionOrContentOptionContaining(writerId, keyword, pageRequest);
    }

    public int getSearchPostTotalCount(Long writerId, String keyword, String searchOption) {
        List<Board> boardEntities;

        if (searchOption.equals("title")) {
            boardEntities = boardRepository.findByWriterIdAndTitleOptionContaining(writerId, keyword);
        } else if (searchOption.equals("content")) {
            boardEntities = boardRepository.findByWriterIdAndContentOptionContaining(writerId, keyword);
        } else {
            boardEntities = boardRepository.findByWriterIdAndTitleOptionOrContentOptionContaining(writerId, keyword);
        }

        if (boardEntities.isEmpty())
            return 0;

        return boardEntities.size();
    }

    public FormUserWithdrawRequestDto findFormUserInfoForWithdrawal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        return new FormUserWithdrawRequestDto(user.getId(), user.getUsername(), null);
    }

    public SocialUserWithdrawRequestDto findSocialUserInfoForWithdrawal() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            return new SocialUserWithdrawRequestDto(user.getId(), user.getEmail(), user.getProvider(), null);
        }

        return null;
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. username = " + username));

        userRepository.delete(user);
    }

    @Transactional
    public void deleteByEmailAndProvider(String email, String provider) {
        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. email = " + email + " provider = " + provider));

        userRepository.delete(user);
    }

    public Long getCurrentUserProfileImageId() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            if (user.getProfileImage() == null) {
                return null;
            }

            return user.getProfileImage().getId();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        if (user.getProfileImage() == null) {
            return null;
        }

        return user.getProfileImage().getId();
    }

    public String getCurrentUserProfileImageStoreName() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            if (user.getProfileImage() == null) {
                return null;
            }

            return user.getProfileImage().getStoreName();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        if (user.getProfileImage() == null) {
            return null;
        }

        return user.getProfileImage().getStoreName();
    }

    @Transactional
    public void deleteProfileImage(Long profileImageId) {
        ProfileImage profileImage = profileImageRepository.findById(profileImageId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 사진이 존재하지 않습니다. id = " + profileImageId));

        profileImageRepository.delete(profileImage);
    }

    public String getUserNickname() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            return user.getNickname();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        return user.getNickname();
    }
}