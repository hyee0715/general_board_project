package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.domain.dto.board.BoardListResponseDto;
import com.hy.general_board_project.domain.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.domain.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.domain.dto.board.BoardUpdateRequestDto;
import com.hy.general_board_project.domain.dto.user.UserSignUpRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    User user;

    @BeforeEach
    public void signUp() {
        UserSignUpRequestDto requestDto =
                UserSignUpRequestDto.builder()
                        .realName("김이름")
                        .username("아이디")
                        .nickname("닉네임")
                        .password("abcd123##")
                        .email("aa@aa")
                        .certified("Y")
                        .build();

        Long userId = userService.joinUser(requestDto);
        user = userRepository.findById(userId).get();
    }

    @Test
    @DisplayName("게시물 새로 작성하기")
    @Transactional
    void save() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer(user.getNickname())
                .content("작성 내용")
                .view(0)
                .writerId(user.getId())
                .user(user)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);
        String title = boardRepository.findById(savedBoardId).get().getTitle();

        Assertions.assertThat(title).isEqualTo("새로 작성 제목");
    }

    @Test
    @DisplayName("작성한 게시물 수정하기")
    @Transactional
    void update() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer(user.getNickname())
                .content("작성 내용")
                .view(0)
                .writerId(user.getId())
                .user(user)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);

        BoardUpdateRequestDto boardUpdateRequestDto = BoardUpdateRequestDto.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();

        Long updatedBoardId = boardService.update(savedBoardId, boardUpdateRequestDto);
        String title = boardRepository.findById(updatedBoardId).get().getTitle();

        Assertions.assertThat(title).isEqualTo("수정 제목");
    }

    @Test
    @DisplayName("한 페이지에 출력하는 게시물 번호 체크하기")
    @Transactional
    void getBoardList() {
        List<Long> savedBoardIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i)
                    .writer(user.getNickname())
                    .content("작성 내용")
                    .view(0)
                    .writerId(user.getId())
                    .user(user)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);
        }

        List<BoardListResponseDto> boardList = boardService.getBoardList(1);
        List<Long> result = boardList.stream().map(BoardListResponseDto::getId).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(savedBoardIds);
    }

    @Test
    @DisplayName("게시물 삭제하기 성공하는 경우")
    @Transactional
    public void deleteSuccess() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer(user.getNickname())
                .content("작성 내용")
                .view(0)
                .writerId(user.getId())
                .user(user)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);
        boardService.delete(savedBoardId);
        Optional<Board> boardEntity = boardRepository.findById(savedBoardId);
        Assertions.assertThat(boardEntity).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 게시물을 삭제하기 시도하는 경우")
    @Transactional
    public void deleteFail() {
        assertThatThrownBy(() -> boardService.delete(100000000000001L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시물 검색하기")
    @Transactional
    public void search() {
        List<Long> savedBoardIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i % 2 + i % 2)
                    .writer(user.getNickname())
                    .content("작성 내용")
                    .view(0)
                    .writerId(user.getId())
                    .user(user)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);
        }

        List<BoardSearchResponseDto> searchResult = boardService.search("테스트00", 1, "title");

        List<String> result = searchResult.stream().map(BoardSearchResponseDto::getTitle).collect(Collectors.toList());

        Assertions.assertThat(result).contains("테스트00");
    }
}