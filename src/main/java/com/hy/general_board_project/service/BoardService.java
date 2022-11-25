package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.web.dto.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.BoardListResponseDto;
import com.hy.general_board_project.web.dto.BoardSaveRequestDto;
import com.hy.general_board_project.web.dto.BoardUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardListResponseDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardListResponseDto> boardDtoList = boardList.stream()
                .map(BoardListResponseDto::convertBoardEntityToBoardListResponseDto)
                .collect(Collectors.toList());

        return boardDtoList;
    }

    @Transactional
    public BoardDetailResponseDto getBoardDetail(Long id) {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDetailResponseDto boardDetailResponseDto = BoardDetailResponseDto.convertBoardEntityToBoardDetailResponseDto(board);

        return boardDetailResponseDto;
    }

    @Transactional
    public Long update(BoardUpdateResponseDto boardUpdateResponseDto) {
        return boardRepository.save(boardUpdateResponseDto.toEntity()).getId();
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }
}
