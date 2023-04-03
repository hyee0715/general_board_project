package com.hy.general_board_project.service;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.profileImage.ProfileImageRepository;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.domain.dto.board.BoardListResponseDto;
import com.hy.general_board_project.domain.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.domain.dto.profileImage.ProfileImageDto;
import com.hy.general_board_project.domain.dto.user.FormUserWithdrawRequestDto;
import com.hy.general_board_project.domain.dto.user.SocialUserWithdrawRequestDto;
import com.hy.general_board_project.domain.dto.user.UserInfoUpdateRequestDto;
import com.hy.general_board_project.domain.dto.user.UserPasswordUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingService {

    private static final int POST_COUNT_OF_ONE_PAGE = 10; // 한 페이지에 존재하는 게시글 수

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ProfileImageRepository profileImageRepository;
    private final HttpSession httpSession;
    private final BoardService boardService;
    private final FileStoreService fileStoreService;

    @Transactional
    public String updateUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = getUserByUserInfoUpdateRequestDto(userInfoUpdateRequestDto);

        user.updateNickname(userInfoUpdateRequestDto.getNickname());

        return userInfoUpdateRequestDto.getNickname();
    }

    @Transactional
    public void updateUserProfileImage(UserInfoUpdateRequestDto userInfoUpdateRequestDto, ProfileImage profileImage) {
        User user = getUserByUserInfoUpdateRequestDto(userInfoUpdateRequestDto);

        user.updateProfileImage(profileImage);
    }

    @Transactional
    public User getUserByUserInfoUpdateRequestDto(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        Optional<User> user = userRepository.findByEmailAndProvider(userInfoUpdateRequestDto.getEmail(), userInfoUpdateRequestDto.getProvider());

        if (userInfoUpdateRequestDto.getProvider().isEmpty()) {
            user = userRepository.findByUsername(userInfoUpdateRequestDto.getUsername());
        }

        return user.get();
    }

    public UserInfoUpdateRequestDto getCurrentUserInfoUpdateRequestDto() {
        User user = getCurrentUser();

        return makeUserInfoUpdateRequestDto(user);
    }

    @Transactional
    public User updateUserPassword(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, String encodedNewPassword) {
        Optional<User> user = userRepository.findByUsername(userPasswordUpdateRequestDto.getUsername())
                .map(entity -> entity.updatePassword(encodedNewPassword));

        return userRepository.save(user.get());
    }

    /***
     * 현재 접속한 유저가 Form을 통해 로그인 한 유저인지
     * 소셜 로그인 한 유저인지 확인
     *
     * @return true : Form 로그인 유저
     *         false : 소셜 로그인 유저
     */
    public boolean isFormUser() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user == null) {
            return true;
        }

        return false;
    }

    public User getCurrentUser() {
        if (!isFormUser()) {
            return getUserForSocialUser().get();
        }

        UserDetails userDetails = getUserDetailsForFormUser();
        if (userDetails == null) {
            return null;
        }

        return getUserByUserDetailsForFormUser(userDetails).get();
    }

    @Transactional
    Optional<User> getUserForSocialUser() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        return userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());
    }

    public UserDetails getUserDetailsForFormUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        return (UserDetails) principal;
    }

    @Transactional
    public Optional<User> getUserByUserDetailsForFormUser(UserDetails userDetails) {
        String username = userDetails.getUsername();

        return userRepository.findByUsername(username);
    }

    UserInfoUpdateRequestDto makeUserInfoUpdateRequestDto(User user) {
        if (user.getProfileImage() != null) {
            return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), user.getProfileImage().getId());
        }

        return new UserInfoUpdateRequestDto(user.getId(), user.getRealName(), user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider(), null);
    }

    @Transactional
    public List<Board> getUserBoardList(Long writerId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        return boardRepository.findByWriterId(writerId, pageRequest);
    }

    @Transactional
    public List<BoardListResponseDto> getUserBoardListResponseDto(Long writerId, int pageNum) {
        List<Board> userBoardList = getUserBoardList(writerId, pageNum);

        if (userBoardList.isEmpty()) {
            return new ArrayList<BoardListResponseDto>();
        }

        return userBoardList.stream()
                .map(BoardListResponseDto::convertBoardEntityToBoardListResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long getUserOwnBoardListCount(Long writerId) {
        List<Board> userBoardList = boardRepository.findByWriterId(writerId);

        return (long) userBoardList.size();
    }

    public int getTotalLastPageNum(Long writerId) {
        Double postsTotalCount = Double.valueOf(this.getUserOwnBoardListCount(writerId));

        // 총 게시글의 마지막 페이지 번호 계산 (올림으로 계산)

        return (int) (Math.ceil((postsTotalCount / POST_COUNT_OF_ONE_PAGE)));
    }

    @Transactional
    public List<BoardSearchResponseDto> search(Long writerId, String keyword, int pageNum, String searchOption) {
        List<Board> boardSearchList = searchPostsUsingSort(writerId, keyword, pageNum, searchOption);

        if (boardSearchList.isEmpty()) {
            return new ArrayList<BoardSearchResponseDto>();
        }

        return boardSearchList.stream()
                .map(BoardSearchResponseDto::convertBoardEntityToBoardSearchResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Board> searchPostsUsingSort(Long writerId, String keyword, int pageNum, String searchOption) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (searchOption.equals("title")) {
            return boardRepository.findByWriterIdAndTitleOptionContaining(writerId, keyword, pageRequest);
        }

        if (searchOption.equals("content")) {
            return boardRepository.findByWriterIdAndContentOptionContaining(writerId, keyword, pageRequest);
        }

        return boardRepository.findByWriterIdAndTitleOptionOrContentOptionContaining(writerId, keyword, pageRequest);
    }

    @Transactional
    public List<Board> searchPosts(Long writerId, String keyword, String searchOption) {
        if (searchOption.equals("title")) {
            return boardRepository.findByWriterIdAndTitleOptionContaining(writerId, keyword);
        }

        if (searchOption.equals("content")) {
            return boardRepository.findByWriterIdAndContentOptionContaining(writerId, keyword);
        }

        return boardRepository.findByWriterIdAndTitleOptionOrContentOptionContaining(writerId, keyword);
    }

    public int getSearchPostTotalCount(Long writerId, String keyword, String searchOption) {
        List<Board> boardEntities = searchPosts(writerId, keyword, searchOption);

        if (boardEntities.isEmpty()) {
            return 0;
        }

        return boardEntities.size();
    }

    public FormUserWithdrawRequestDto findFormUserInfoForWithdrawal() {
        UserDetails userDetails = getUserDetailsForFormUser();
        User user = getUserByUserDetailsForFormUser(userDetails).get();

        return new FormUserWithdrawRequestDto(user.getId(), user.getUsername(), null);
    }

    public SocialUserWithdrawRequestDto findSocialUserInfoForWithdrawal() {
        User user = getUserForSocialUser().get();

        return new SocialUserWithdrawRequestDto(user.getId(), user.getEmail(), user.getProvider(), null);
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. username = " + username));

        boardService.deleteUserRelation(user.getId());
        userRepository.delete(user);
    }

    @Transactional
    public void deleteByEmailAndProvider(String email, String provider) {
        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. email = " + email + " provider = " + provider));

        boardService.deleteUserRelation(user.getId());
        userRepository.delete(user);
    }

    public Long getCurrentUserProfileImageId() {
        User user = getCurrentUser();

        return getProfileImageIdByUser(user);
    }

    public Long getProfileImageIdByUser(User user) {
        if (user.getProfileImage() == null) {
            return null;
        }

        return user.getProfileImage().getId();
    }

    public String getUserProfileImageStoreNameByUser(User user) {
        if (user.getProfileImage() == null) {
            return null;
        }

        return user.getProfileImage().getStoreName();
    }

    public String getCurrentUserProfileImageStoreName() {
        User user = getCurrentUser();

        if (user == null) {
            return null;
        }

        return getUserProfileImageStoreNameByUser(user);
    }

    public String getProfileImageStoreNameForDownload(String storeName) {
        if (storeName.isEmpty()) {
            return null;
        }

        String[] names = storeName.split("/");
        return names[names.length - 1];
    }

    @Transactional
    public void deleteProfileImage(Long profileImageId) {
        ProfileImage profileImage = profileImageRepository.findById(profileImageId)
                .orElseThrow(() -> new IllegalArgumentException("프로필 사진이 존재하지 않습니다. id = " + profileImageId));

        profileImageRepository.delete(profileImage);
    }

    public String getUserNickname() {
        User user = getCurrentUser();

        if (user == null) {
            return null;
        }
        return user.getNickname();
    }

    @Transactional
    public void updateUserNicknameAndBoardWriter(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        String newUserNickname = updateUserNickname(userInfoUpdateRequestDto);

        //해당 사용자의 모든 게시물 작성자 이름 수정
        boardService.updateBoardWriter(newUserNickname);
    }

    @Transactional
    public void deleteUserProfileImage(Long currentUserProfileImageId) {
        if (currentUserProfileImageId != null) {
            fileStoreService.deleteStoredFile(currentUserProfileImageId);

            deleteProfileImage(currentUserProfileImageId);
        }
    }

    @Transactional
    public void updateNewProfileImage(UserInfoUpdateRequestDto userInfoUpdateRequestDto) throws IOException {
        //새 프로필 사진 저장
        ProfileImageDto profileImageDto = fileStoreService.storeFile(userInfoUpdateRequestDto.getProfileImage());
        ProfileImage profileImage = fileStoreService.save(profileImageDto);

        updateUserProfileImage(userInfoUpdateRequestDto, profileImage);
    }

    public User findUser() {
        UserDetails userDetails = getUserDetailsForFormUser();
        Optional<User> userEntity = getUserByUserDetailsForFormUser(userDetails);

        return userEntity.get();
    }
}