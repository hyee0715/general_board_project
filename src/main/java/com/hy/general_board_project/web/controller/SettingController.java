package com.hy.general_board_project.web.controller;

import static com.hy.general_board_project.service.MessageService.showMessageAndRedirect;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.*;
import com.hy.general_board_project.validator.CheckNicknameModificationValidator;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.message.MessageDto;
import com.hy.general_board_project.web.dto.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SettingController {

    private final SettingService settingService;
    private final CheckNicknameModificationValidator checkNicknameModificationValidator;
    private final BoardService boardService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FileStoreService fileStoreService;
    private final ValidationService validationService;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkNicknameModificationValidator);
    }

    @GetMapping("/setting/userInfo")
    public String moveToUserInfo(Model model) {
        model.addAttribute("currentNickname", settingService.getUserNickname());

        UserInfoUpdateRequestDto userInfoUpdateRequestDto = settingService.getCurrentUserInfoUpdateRequestDto();
        model.addAttribute("userInfoUpdateRequestDto", userInfoUpdateRequestDto);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!userService.isCertifiedUser()) {
                return "redirect:/user/emailCertified";
            }

            model.addAttribute("formUser", true);
            return "setting/userInfo";
        }

        model.addAttribute("formUser", false);
        return "setting/userInfo";
    }

    @PostMapping("/setting/userInfo")
    public String updateUserInfo(@Validated @ModelAttribute UserInfoUpdateRequestDto userInfoUpdateRequestDto, BindingResult bindingResult, Model model) throws IOException {

        bindingResult = validationService.validateNicknameForUpdateUserInfo(userInfoUpdateRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors ={}", bindingResult);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);
            model.addAttribute("currentNickname", settingService.getUserNickname());

            if (settingService.isFormUser()) {
                model.addAttribute("formUser", true);
                return "setting/userInfo";
            }

            model.addAttribute("formUser", false);
            return "setting/userInfo";
        }

        //새 닉네임으로 변경, 해당 사용자의 모든 게시물 작성자 이름 변경
        settingService.updateUserNicknameAndBoardWriter(userInfoUpdateRequestDto);

        //프로필 사진 설정 되어 있는 상태에서 기본 프로필 사진으로 돌아가는 경우
        if (userInfoUpdateRequestDto.getProfileImageId() != null && userInfoUpdateRequestDto.getProfileImageId() == -1) {
            Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();
            userService.deleteProfileImage(userInfoUpdateRequestDto.getId());
            settingService.deleteUserProfileImage(currentUserProfileImageId);

            MessageDto message = new MessageDto("회원 정보 수정이 완료되었습니다.", "/setting/userInfo", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //프로필 사진 설정 되어 있는 상태에서 다른 사진으로 변경하는 경우 or 기본 프로필 사진에서 새로운 사진으로 변경하는 경우
        if (!userInfoUpdateRequestDto.getProfileImage().isEmpty()) {
            Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();
            userService.deleteProfileImage(userInfoUpdateRequestDto.getId());
            settingService.deleteUserProfileImage(currentUserProfileImageId);

            settingService.updateNewProfileImage(userInfoUpdateRequestDto);
        }

        MessageDto message = new MessageDto("회원 정보 수정이 완료되었습니다.", "/setting/userInfo", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/setting/userList")
    public String moveToUserList(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        model.addAttribute("currentNickname", settingService.getUserNickname());
        model.addAttribute("curPage", pageNum);

        String writerNickname = settingService.getCurrentUserInfoUpdateRequestDto().getNickname();
        model.addAttribute("nickname", writerNickname);

        Long writerId = userRepository.findByNickname(writerNickname).get().getId();
        List<BoardListResponseDto> userOwnBoardList = settingService.getUserBoardListResponseDto(writerId, pageNum);
        model.addAttribute("boardList", userOwnBoardList);

        int totalLastPageNum = settingService.getTotalLastPageNum(writerId);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);
        model.addAttribute("pageList", pageList);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!userService.isCertifiedUser()) {
                return "redirect:/user/emailCertified";
            }

            model.addAttribute("formUser", true);
            return "setting/userList";
        }

        model.addAttribute("formUser", false);
        return "setting/userList";
    }

    @GetMapping("/setting/userPassword")
    public String moveToUserPassword(Model model) {
        if (!userService.isCertifiedUser()) {
            return "redirect:/user/emailCertified";
        }

        model.addAttribute("currentNickname", settingService.getUserNickname());

        UserInfoUpdateRequestDto userInfoUpdateRequestDto = settingService.getCurrentUserInfoUpdateRequestDto();
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = userInfoUpdateRequestDto.convertToPasswordUpdateDto();
        model.addAttribute("userPasswordUpdateRequestDto", userPasswordUpdateRequestDto);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        String nickname = settingService.getUserNickname();
        model.addAttribute("nickname", nickname);

        return "setting/userPassword";
    }

    @PostMapping("/setting/userPassword")
    public String updateUserPassword(@Validated @ModelAttribute UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, BindingResult bindingResult, Model model) {

        User currentUser = settingService.findUser();

        bindingResult = validationService.validateCurrentPasswordForUpdatePassword(userPasswordUpdateRequestDto, bindingResult, currentUser);
        bindingResult = validationService.validateNewPasswordForUpdatePassword(userPasswordUpdateRequestDto, bindingResult, currentUser);
        bindingResult = validationService.validateNewPasswordConfirmForUpdatePassword(userPasswordUpdateRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);
            model.addAttribute("currentNickname", settingService.getUserNickname());

            return "setting/userPassword";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedNewPassword = encoder.encode(userPasswordUpdateRequestDto.getNewPassword());
        settingService.updateUserPassword(userPasswordUpdateRequestDto, encodedNewPassword);

        MessageDto message = new MessageDto("비밀번호 변경이 완료되었습니다.", "/setting/userPassword", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/setting/userList/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model, @RequestParam(value = "searchOption", required = false) String searchOption, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        model.addAttribute("currentNickname", settingService.getUserNickname());
        model.addAttribute("keyword", keyword);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("searchOption", searchOption);

        String writerNickname = settingService.getCurrentUserInfoUpdateRequestDto().getNickname();
        Long writerId = userRepository.findByNickname(writerNickname).get().getId();
        List<BoardSearchResponseDto> boardSearchDtoList = settingService.search(writerId, keyword, pageNum, searchOption);
        model.addAttribute("boardList", boardSearchDtoList);

        int searchPostTotalCount = settingService.getSearchPostTotalCount(writerId, keyword, searchOption);
        int totalLastPageNum = boardService.getTotalLastSearchPageNum(searchPostTotalCount);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);
        model.addAttribute("pageList", pageList);

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!userService.isCertifiedUser()) {
                return "redirect:/user/emailCertified";
            }

            model.addAttribute("formUser", true);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);

            String nickname = settingService.getUserNickname();
            model.addAttribute("nickname", nickname);

            return "setting/search";
        }

        model.addAttribute("formUser", false);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        String nickname = settingService.getUserNickname();
        model.addAttribute("nickname", nickname);

        return "setting/search";
    }

    @PostMapping("/setting/userList/delete")
    public String deleteBoards(@RequestParam List<String> boardIds) {
        for (int i = 0; i < boardIds.size(); i++) {
            Long id = Long.valueOf(boardIds.get(i));
            boardService.delete(id);
        }

        return "redirect:/setting/userList";
    }

    @GetMapping("/setting/withdrawal")
    public String withdraw(Model model) {
        model.addAttribute("currentNickname", settingService.getUserNickname());

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!userService.isCertifiedUser()) {
                return "redirect:/user/emailCertified";
            }

            model.addAttribute("formUser", true);

            FormUserWithdrawRequestDto formUserWithdrawRequestDto = settingService.findFormUserInfoForWithdrawal();
            model.addAttribute("formUserWithdrawRequestDto", formUserWithdrawRequestDto);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);

            String nickname = settingService.getUserNickname();
            model.addAttribute("nickname", nickname);

            return "setting/withdrawal";
        }

        model.addAttribute("formUser", false);

        SocialUserWithdrawRequestDto socialUserWithdrawRequestDto = settingService.findSocialUserInfoForWithdrawal();
        model.addAttribute("socialUserWithdrawRequestDto", socialUserWithdrawRequestDto);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);

        String nickname = settingService.getUserNickname();
        model.addAttribute("nickname", nickname);

        return "setting/withdrawal";
    }

    @PostMapping("/setting/withdrawal/password")
    public String checkWithdrawalPassword(@Validated @ModelAttribute FormUserWithdrawRequestDto formUserWithdrawRequestDto, BindingResult bindingResult, Model model) {
        User currentUser = settingService.findUser();

        bindingResult = validationService.validatePasswordForWithdrawal(formUserWithdrawRequestDto, bindingResult, currentUser);

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);
            model.addAttribute("formUser", true);
            model.addAttribute("currentNickname", settingService.getUserNickname());

            return "setting/withdrawal";
        }

        Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();
        userService.deleteProfileImage(formUserWithdrawRequestDto.getId());
        settingService.deleteUserProfileImage(currentUserProfileImageId);
        settingService.deleteByUsername(formUserWithdrawRequestDto.getUsername());

        MessageDto message = new MessageDto("회원 탈퇴가 완료되었습니다.", "/logout", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/setting/withdrawal/email")
    public String checkWithdrawalEmail(@Validated @ModelAttribute SocialUserWithdrawRequestDto socialUserWithdrawRequestDto, BindingResult bindingResult, Model model) {

        bindingResult = validationService.validateEmailForWithdrawal(socialUserWithdrawRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);
            model.addAttribute("formUser", false);
            model.addAttribute("currentNickname", settingService.getUserNickname());

            return "setting/withdrawal";
        }

        Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();
        userService.deleteProfileImage(socialUserWithdrawRequestDto.getId());
        settingService.deleteUserProfileImage(currentUserProfileImageId);
        settingService.deleteByEmailAndProvider(socialUserWithdrawRequestDto.getEmail(), socialUserWithdrawRequestDto.getProvider());

        MessageDto message = new MessageDto("회원 탈퇴가 완료되었습니다.", "/logout", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
