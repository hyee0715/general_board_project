package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.web.dto.BoardDetailResponseDto;
import com.hy.general_board_project.web.dto.BoardListResponseDto;
import com.hy.general_board_project.web.dto.BoardSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<BoardListResponseDto> boardDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boardDtoList.add(BoardListResponseDto.convertBoardEntityToBoardListResponseDto(board));
        }

        return boardDtoList;
    }

    @Transactional
    public BoardDetailResponseDto getBoardDetail(Long id) {
        Optional<Board> boardWrapper = boardRepository.findById(id);
        Board board = boardWrapper.get();

        BoardDetailResponseDto boardDetailResponseDto = BoardDetailResponseDto.convertBoardEntityToBoardDetailResponseDto(board);

        return boardDetailResponseDto;
    }
}
