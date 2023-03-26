package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.board.BoardUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    List<Long> afterDeleteIds = new ArrayList<>();

    @AfterEach
    void deleteAfterTest() {
        for (long id : afterDeleteIds) {
            boardRepository.deleteById(id);
        }

        afterDeleteIds.clear();
    }

    @Test
    @DisplayName("게시물 새로 작성하기")
    void save() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer("글쓴이")
                .content("작성 내용")
                .view(0)
                .writerId(100001L)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);
        String title = boardRepository.findById(savedBoardId).get().getTitle();
        afterDeleteIds.add(savedBoardId);

        Assertions.assertThat(title).isEqualTo("새로 작성 제목");
    }

    @Test
    @DisplayName("작성한 게시물 수정하기")
    void update() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer("글쓴이")
                .content("작성 내용")
                .view(0)
                .writerId(100001L)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);

        BoardUpdateRequestDto boardUpdateRequestDto = BoardUpdateRequestDto.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();

        Long updatedBoardId = boardService.update(savedBoardId, boardUpdateRequestDto);
        String title = boardRepository.findById(updatedBoardId).get().getTitle();
        afterDeleteIds.add(updatedBoardId);

        Assertions.assertThat(title).isEqualTo("수정 제목");
    }

    @Test
    @DisplayName("한 페이지에 출력하는 게시물 번호 체크하기")
    void getBoardList() {
        List<Long> savedBoardIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i)
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            afterDeleteIds.add(savedBoardId);
        }

        List<BoardListResponseDto> boardList = boardService.getBoardList(1);
        List<Long> result = boardList.stream().map(BoardListResponseDto::getId).collect(Collectors.toList());

        Assertions.assertThat(result).containsAll(savedBoardIds);
    }

    @Test
    @DisplayName("게시물 삭제하기 성공하는 경우")
    public void deleteSuccess() {
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder()
                .title("새로 작성 제목")
                .writer("글쓴이")
                .content("작성 내용")
                .view(0)
                .writerId(100001L)
                .build();

        Long savedBoardId = boardService.save(boardSaveRequestDto);
        boardService.delete(savedBoardId);
        Optional<Board> boardEntity = boardRepository.findById(savedBoardId);
        Assertions.assertThat(boardEntity).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 게시물을 삭제하기 시도하는 경우")
    public void deleteFail() {
        assertThatThrownBy(() -> boardService.delete(100000000000001L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시물 검색하기")
    public void search() {
        List<Long> savedBoardIds = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            BoardSaveRequestDto saveBoards = BoardSaveRequestDto.builder()
                    .title("테스트" + i % 2 + i % 2)
                    .writer("글쓴이")
                    .content("작성 내용")
                    .view(0)
                    .writerId(100001L)
                    .build();

            Long savedBoardId = boardService.save(saveBoards);
            savedBoardIds.add(savedBoardId);

            afterDeleteIds.add(savedBoardId);
        }

        List<BoardSearchResponseDto> searchResult = boardService.search("테스트00", 1, "title");

        List<String> result = searchResult.stream().map(BoardSearchResponseDto::getTitle).collect(Collectors.toList());

        Assertions.assertThat(result).contains("테스트00");
    }
}