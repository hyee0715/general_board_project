package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.board.Board;
import com.hy.general_board_project.domain.board.BoardRepository;
import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.board.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private static final int PAGE_NUMBER_COUNT_OF_ONE_BLOCK = 8; // 한 블럭에 존재하는 페이지 번호 개수
    private static final int PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK = PAGE_NUMBER_COUNT_OF_ONE_BLOCK / 2;
    private static final int POST_COUNT_OF_ONE_PAGE = 10; // 한 페이지에 존재하는 게시글 수

    private static final int DEFAULT_START_PAGE_NUMBER = 1;

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardListResponseDto> getBoardList(int pageNum) {
        Page<Board> page = boardRepository.findAll(PageRequest.of(
                pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate")
        ));

        List<Board> boardList = page.getContent();

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
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        board.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        boardRepository.delete(board);
    }

    @Transactional
    public List<Board> makeBoardSearchList(String keyword, int pageNum, String searchOption) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, POST_COUNT_OF_ONE_PAGE, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (searchOption.equals("title")) {
            return boardRepository.findByTitleContaining(keyword, pageRequest);
        }

        if (searchOption.equals("content")) {
            return boardRepository.findByContentContaining(keyword, pageRequest);
        }

        if (searchOption.equals("writer")) {
            return boardRepository.findByWriterContaining(keyword, pageRequest);
        }

        return boardRepository.findByAllOptionContaining(keyword, pageRequest);
    }

    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    /***
     *
     * @return 총 게시물의 마지막 페이지 번호
     */
    public Integer getTotalLastPageNum() {
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        // 총 게시글의 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int) (Math.ceil((postsTotalCount / POST_COUNT_OF_ONE_PAGE)));

        return totalLastPageNum;
    }

    public List<Integer> getPageList(int curPageNum, int totalLastPageNum) {
        if (totalLastPageNum < PAGE_NUMBER_COUNT_OF_ONE_BLOCK) {
            return makePageList(DEFAULT_START_PAGE_NUMBER, totalLastPageNum);
        }

        int lastPageNumberOfCurrentBlock = findLastPageNumberOfCurrentBlock(curPageNum, totalLastPageNum);
        int pageStartNumber = updatePageStartNumberOfCurrentPage(curPageNum, totalLastPageNum);

        return makePageList(pageStartNumber, lastPageNumberOfCurrentBlock);
    }

    public List<Integer> makePageList(int pageStartNumber, int pageLastNumber) {
        List<Integer> pageList = new ArrayList<>();

        for (int number = pageStartNumber; number <= pageLastNumber; number++) {
            pageList.add(number);
        }

        return pageList;
    }

    public int findLastPageNumberOfCurrentBlock(int curPageNum, int totalLastPageNum) {
        if (curPageNum < PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK) {
            return PAGE_NUMBER_COUNT_OF_ONE_BLOCK;
        }

        int calculatedCurrentLastPageNumber = curPageNum + PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK;
        //현재 페이지에 띄울 마지막 페이지 번호가 총 페이지 번호를 넘지 않는다면 업데이트
        if (calculatedCurrentLastPageNumber < totalLastPageNum) {
            return calculatedCurrentLastPageNumber;
        }

        return totalLastPageNum;
    }

    public int updatePageStartNumberOfCurrentPage(int curPageNum, int totalLastPageNum) {
        if (curPageNum < PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK) {
            return DEFAULT_START_PAGE_NUMBER;
        }

        int pageNumberEndZone = totalLastPageNum - PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK;
        if (curPageNum > pageNumberEndZone) {
            return totalLastPageNum - PAGE_NUMBER_COUNT_OF_ONE_BLOCK + 1;
        }

        return curPageNum - PAGE_NUMBER_HALF_COUNT_OF_ONE_BLOCK + 1;
    }

    @Transactional
    public List<BoardSearchResponseDto> search(String keyword, int pageNum, String searchOption) {
        List<Board> boardSearchList = makeBoardSearchList(keyword, pageNum, searchOption);

        List<BoardSearchResponseDto> boardSearchDtoList = new ArrayList<>();

        if (boardSearchList.isEmpty()) return boardSearchDtoList;

        boardSearchList.stream()
                .map(BoardSearchResponseDto::convertBoardEntityToBoardSearchResponseDto)
                .forEach(boardSearchDtoList::add);

        return boardSearchDtoList;
    }

    public int getSearchPostTotalCount(String keyword, String searchOption) {
        List<Board> boardEntities;

        if (searchOption.equals("title")) {
            boardEntities = boardRepository.findByTitleContaining(keyword);
        } else if (searchOption.equals("content")) {
            boardEntities = boardRepository.findByContentContaining(keyword);
        } else if (searchOption.equals("writer")) {
            boardEntities = boardRepository.findByWriterContaining(keyword);
        } else {
            boardEntities = boardRepository.findByAllOptionContaining(keyword);
        }

        if (boardEntities.isEmpty()) return 0;
        return boardEntities.size();
    }

    public Integer getTotalLastSearchPageNum(Integer searchPostTotalCount) {
        Double postsTotalCount = Double.valueOf(searchPostTotalCount);

        Integer totalLastSearchPageNum = (int) (Math.ceil((postsTotalCount / POST_COUNT_OF_ONE_PAGE)));

        return totalLastSearchPageNum;
    }

    @Transactional
    public void updateBoardWriter(String newWriterName) {
        Optional<User> writer = userRepository.findByNickname(newWriterName);
        Long writerId = writer.get().getId();

        List<Board> posts = boardRepository.findByWriterId(writerId);

        for (Board post : posts) {
            post.updateWriter(newWriterName);
        }
    }

    @Transactional
    public Integer updateView(Long id) {
        return boardRepository.updateView(id);
    }

    @Transactional
    public Board getBoard(Long id) {
        return boardRepository.findById(id).get();
    }

    public String getWriterProfileImageStoreName(User writer) {
        ProfileImage profileImage = writer.getProfileImage();

        if (profileImage == null) {
            return null;
        }

        return profileImage.getStoreName();
    }
}
