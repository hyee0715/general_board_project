package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.profileImage.ProfileImageRepository;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.profileImage.ProfileImageDto;
import com.hy.general_board_project.web.dto.user.UserPasswordUpdateRequestDto;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class SettingServiceTest {

    @Autowired
    SettingService settingService;

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileImageRepository profileImageRepository;

    List<Long> afterDeleteBoardIds = new ArrayList<>();
    List<Long> afterDeleteUserIds = new ArrayList<>();

    @AfterEach
    void deleteAfterTest() {
        for (long id : afterDeleteBoardIds) {
            boardRepository.deleteById(id);
        }
        afterDeleteBoardIds.clear();

        for (long id : afterDeleteUserIds) {
            userRepository.deleteById(id);
        }
        afterDeleteUserIds.clear();
    }

    @Test
    @DisplayName("비밀번호 변경하기")
    public void updateUserPassword() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("testUser1")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aabc@aabcd.com")
                        .build();

        Long userId = userService.joinUser(requestDto);
        afterDeleteUserIds.add(userId);

        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = UserPasswordUpdateRequestDto.builder()
                .username("testUser1")
                .currentPassword("abcd123##")
                .newPassword("dddd2234##")
                .newPasswordConfirm("dddd2234##")
                .build();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedNewPassword = encoder.encode(userPasswordUpdateRequestDto.getNewPassword());

        User user = settingService.updateUserPassword(userPasswordUpdateRequestDto, encodedNewPassword);

        Assertions.assertThat(user.getPassword()).isEqualTo(encodedNewPassword);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물만 조회하기")
    public void getUserBoardList() {
        List<Long> savedBoardIds = new ArrayList<>();
        List<String> boardTitle = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i)
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L + (i % 2))
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            if (i % 2 == 0) {
                boardTitle.add("테스트" + i);
            }

            afterDeleteBoardIds.add(savedBoardId);
        }

        List<String> result = settingService.getUserBoardList(100001L, 1).stream().map(Board::getTitle).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(boardTitle);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물 개수 조회하기")
    public void getUserOwnBoardListCount() {
        List<Long> savedBoardIds = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i)
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L + (i % 2))
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            afterDeleteBoardIds.add(savedBoardId);
        }

        Long result = settingService.getUserOwnBoardListCount(100001L);

        Assertions.assertThat(result).isEqualTo(6);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물 마지막 페이지 번호 구하기")
    public void getTotalLastPageNum() {
        List<Long> savedBoardIds = new ArrayList<>();

        for (int i = 1; i <= 42; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i)
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L + (i % 2))
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            afterDeleteBoardIds.add(savedBoardId);
        }

        int result = settingService.getTotalLastPageNum(100001L);

        Assertions.assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물 제목 검색하기")
    public void searchTitle() {
        List<Long> savedBoardIds = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + (i % 2))
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            if (i % 2 == 0) {
                titles.add(saveBoards.getTitle());
            }

            afterDeleteBoardIds.add(savedBoardId);
        }

        List<String> result = settingService.search(100001L, "테스트0", 1, "title").stream().map(BoardSearchResponseDto::getTitle).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(titles);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물 내용 정렬하여 검색하기")
    public void searchContentsUsingSort() {
        List<Long> savedBoardIds = new ArrayList<>();
        List<String> contents = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트")
                    .writer("글쓴이")
                    .content("작성 내용" + i)
                    .view(0)
                    .writerId(100001L)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            contents.add(saveBoards.getContent());

            afterDeleteBoardIds.add(savedBoardId);
        }

        List<String> result = settingService.searchPostsUsingSort(100001L, "작성 내용", 1, "content").stream().map(Board::getContent).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(contents);
    }

    @Test
    @DisplayName("현재 로그인한 사용자의 게시물 내용 검색하기")
    public void searchContents() {
        List<Long> savedBoardIds = new ArrayList<>();
        List<String> contents = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트")
                    .writer("글쓴이")
                    .content("작성 내용" + i)
                    .view(0)
                    .writerId(100001L)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            contents.add(saveBoards.getContent());

            afterDeleteBoardIds.add(savedBoardId);
        }

        List<String> result = settingService.searchPosts(100001L, "작성 내용", "content").stream().map(Board::getContent).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(contents);
    }

    @Test
    @DisplayName("아이디로 계정 삭제하기 성공하는 경우")
    public void deleteByUsername() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("testUser1")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aabc@aabcd.com")
                        .build();

        userService.joinUser(requestDto);
        settingService.deleteByUsername(requestDto.getUsername());
        Optional<User> user = userRepository.findByUsername(requestDto.getUsername());

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 계정의 아이디를 이용하여 삭제 시도하는 경우")
    public void deleteByUsernameFail() {
        Assertions.assertThatThrownBy(() -> settingService.deleteByUsername("testUser1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 사용자가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이메일과 Provider로 계정 삭제하기 성공하는 경우")
    public void deleteByEmailAndProvider() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("testUser1")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aabc@aabcd.com")
                        .build();

        userService.joinUser(requestDto);
        settingService.deleteByEmailAndProvider(requestDto.getEmail(), null);
        Optional<User> user = userRepository.findByEmailAndProvider(requestDto.getEmail(), null);

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 계정의 이메일과 Provider를 이용하여 삭제 시도하는 경우")
    public void deleteByEmailAndProviderFail() {
        Assertions.assertThatThrownBy(() -> settingService.deleteByEmailAndProvider("aabc@aabcd.com", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 사용자가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("프로필 사진 삭제하기 성공하는 경우")
    public void deleteProfileImage() {
        ProfileImageDto profileImageDto = ProfileImageDto.builder()
                .uploadName("업로드이름")
                .storeName("저장이름")
                .build();

        Long profileImageId = profileImageRepository.save(profileImageDto.toEntity()).getId();

        settingService.deleteProfileImage(profileImageId);
        Optional<ProfileImage> profileImage = profileImageRepository.findById(profileImageId);

        Assertions.assertThat(profileImage).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 프로필 사진을 삭제 시도하는 경우")
    public void deleteProfileImageFail() {
        Assertions.assertThatThrownBy(() -> settingService.deleteProfileImage(1000000000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("프로필 사진이 존재하지 않습니다.");
    }
}
