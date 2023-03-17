package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.domain.profileImage.ProfileImage;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.service.BoardService;
import com.hy.general_board_project.service.FileStoreService;
import com.hy.general_board_project.service.SettingService;
import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.validator.CheckNicknameModificationValidator;
import com.hy.general_board_project.web.dto.board.BoardListResponseDto;
import com.hy.general_board_project.web.dto.board.BoardSearchResponseDto;
import com.hy.general_board_project.web.dto.message.MessageDto;
import com.hy.general_board_project.web.dto.profileImage.ProfileImageDto;
import com.hy.general_board_project.web.dto.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
            if (!isCertifiedUser()) {
                return "redirect:/user/emailCertified";
            }

            model.addAttribute("formUser", true);
            return "setting/userInfo";
        }

        model.addAttribute("formUser", false);
        return "setting/userInfo";
    }

    @PostMapping("/setting/userInfo")
    public String UpdateUserInfo(@Validated @ModelAttribute UserInfoUpdateRequestDto userInfoUpdateRequestDto, BindingResult bindingResult, Model model) throws IOException {

        if (!StringUtils.hasText(userInfoUpdateRequestDto.getNickname())) {
            bindingResult.rejectValue("nickname", "required", "");
        } else {
            if (!Pattern.matches("^[가-힣a-zA-Z0-9]{2,10}$", userInfoUpdateRequestDto.getNickname())) {
                bindingResult.addError(new FieldError("userInfoUpdateRequestDto", "nickname", userInfoUpdateRequestDto.getNickname(), false, null, null, "닉네임은 특수문자 미포함 2~10자리 제한입니다."));
            }
        }

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

        String newUserNickname = settingService.updateUserNickname(userInfoUpdateRequestDto);

        //해당 사용자의 모든 게시물 작성자 이름 수정
        boardService.updateBoardWriter(newUserNickname);

        //프로필 사진 설정 되어 있는 상태에서 기본 프로필 사진으로 돌아가는 경우
        if (userInfoUpdateRequestDto.getProfileImageId() != null && userInfoUpdateRequestDto.getProfileImageId() == -1) {
            Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();

            userService.deleteProfileImage(userInfoUpdateRequestDto.getId());

            if (currentUserProfileImageId != null) {
                fileStoreService.deleteStoredFile(currentUserProfileImageId);
                settingService.deleteProfileImage(currentUserProfileImageId);
            }

            MessageDto message = new MessageDto("회원 정보 수정이 완료되었습니다.", "/setting/userInfo", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        //프로필 사진 설정 되어 있는 상태에서 다른 사진으로 변경하는 경우
        if (!userInfoUpdateRequestDto.getProfileImage().isEmpty()) {
            Long currentUserProfileImageId = settingService.getCurrentUserProfileImageId();

            userService.deleteProfileImage(userInfoUpdateRequestDto.getId());

            if (currentUserProfileImageId != null) {
                fileStoreService.deleteStoredFile(currentUserProfileImageId);
                settingService.deleteProfileImage(currentUserProfileImageId);
            }

            //새 프로필 사진 저장
            ProfileImageDto profileImageDto = fileStoreService.storeFile(userInfoUpdateRequestDto.getProfileImage());
            ProfileImage profileImage = fileStoreService.save(profileImageDto);

            settingService.updateUserProfileImage(userInfoUpdateRequestDto, profileImage);
        }

        MessageDto message = new MessageDto("회원 정보 수정이 완료되었습니다.", "/setting/userInfo", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:///" + fileStoreService.getFullPath(filename));
    }

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }

    @GetMapping("/setting/userList")
    public String moveToUserList(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        model.addAttribute("currentNickname", settingService.getUserNickname());

        String writerNickname = settingService.getCurrentUserInfoUpdateRequestDto().getNickname();

        Long writerId = userRepository.findByNickname(writerNickname).get().getId();

        List<BoardListResponseDto> userOwnBoardList = settingService.getUserBoardListResponseDto(writerId, pageNum);

        int totalLastPageNum = settingService.getTotalLastPageNum(writerId);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("boardList", userOwnBoardList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);

        String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
        model.addAttribute("profileImageStoreName", profileImageStoreName);
        model.addAttribute("nickname", writerNickname);

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!isCertifiedUser()) {
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
        if (!isCertifiedUser()) {
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User currentUser = findUser();

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "required", "");
        } else {
            if (!encoder.matches(userPasswordUpdateRequestDto.getCurrentPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "currentPassword", userPasswordUpdateRequestDto.getCurrentPassword(), false, null, null, "비밀번호가 일치하지 않습니다."));
            }
        }

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getNewPassword())) {
            bindingResult.rejectValue("newPassword", "required", "");
        } else {
            //현재 비밀번호와 동일한 비밀번호를 입력했을 경우
            if (encoder.matches(userPasswordUpdateRequestDto.getNewPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPassword", userPasswordUpdateRequestDto.getNewPassword(), false, null, null, "기존과 동일한 비밀번호 입니다."));
            } else if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", userPasswordUpdateRequestDto.getNewPassword())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPassword", userPasswordUpdateRequestDto.getNewPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            }
        }

        if (!StringUtils.hasText(userPasswordUpdateRequestDto.getNewPasswordConfirm())) {
            bindingResult.rejectValue("newPasswordConfirm", "required", "");
        } else {
            if (!userPasswordUpdateRequestDto.getNewPassword().equals(userPasswordUpdateRequestDto.getNewPasswordConfirm())) {
                bindingResult.addError(new FieldError("userPasswordUpdateRequestDto", "newPasswordConfirm", userPasswordUpdateRequestDto.getNewPasswordConfirm(), false, null, null, "새 비밀번호와 일치하지 않습니다."));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);

            String profileImageStoreName = settingService.getCurrentUserProfileImageStoreName();
            model.addAttribute("profileImageStoreName", profileImageStoreName);
            model.addAttribute("currentNickname", settingService.getUserNickname());

            return "setting/userPassword";
        }

        String encodedNewPassword = encoder.encode(userPasswordUpdateRequestDto.getNewPassword());

        settingService.updateUserPassword(userPasswordUpdateRequestDto, encodedNewPassword);

        MessageDto message = new MessageDto("비밀번호 변경이 완료되었습니다.", "/setting/userPassword", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    public User findUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        return userEntity.get();
    }

    @GetMapping("/setting/userList/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model, @RequestParam(value = "searchOption", required = false) String searchOption, @RequestParam(value = "page", defaultValue = "1") int pageNum) {
        model.addAttribute("currentNickname", settingService.getUserNickname());

        String writerNickname = settingService.getCurrentUserInfoUpdateRequestDto().getNickname();

        Long writerId = userRepository.findByNickname(writerNickname).get().getId();

        List<BoardSearchResponseDto> boardSearchDtoList = settingService.search(writerId, keyword, pageNum, searchOption);

        int searchPostTotalCount = settingService.getSearchPostTotalCount(writerId, keyword, searchOption);

        int totalLastPageNum = boardService.getTotalLastSearchPageNum(searchPostTotalCount);

        List<Integer> pageList = boardService.getPageList(pageNum, totalLastPageNum);

        model.addAttribute("boardList", boardSearchDtoList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("totalLastPageNum", totalLastPageNum);
        model.addAttribute("searchOption", searchOption);

        boolean isFormUser = settingService.isFormUser();

        if (isFormUser) {
            if (!isCertifiedUser()) {
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
    public String delete(@RequestParam List<String> boardIds) {
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
            if (!isCertifiedUser()) {
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User currentUser = findUser();

        if (!StringUtils.hasText(formUserWithdrawRequestDto.getRequestPassword())) {
            bindingResult.rejectValue("requestPassword", "required", "");
        } else {
            if (!Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", formUserWithdrawRequestDto.getRequestPassword())) {
                bindingResult.addError(new FieldError("formUserWithdrawRequestDto", "requestPassword", formUserWithdrawRequestDto.getRequestPassword(), false, null, null, "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상 포함 8자 ~ 20자 제한입니다."));
            } else if (!encoder.matches(formUserWithdrawRequestDto.getRequestPassword(), currentUser.getPassword())) {
                bindingResult.addError(new FieldError("formUserWithdrawRequestDto", "requestPassword", formUserWithdrawRequestDto.getRequestPassword(), false, null, null, "비밀번호가 일치하지 않습니다."));
            }
        }

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

        if (currentUserProfileImageId != null) {
            fileStoreService.deleteStoredFile(currentUserProfileImageId);
            settingService.deleteProfileImage(currentUserProfileImageId);
        }

        settingService.deleteByUsername(formUserWithdrawRequestDto.getUsername());

        MessageDto message = new MessageDto("회원 탈퇴가 완료되었습니다.", "/logout", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/setting/withdrawal/email")
    public String checkWithdrawalEmail(@Validated @ModelAttribute SocialUserWithdrawRequestDto socialUserWithdrawRequestDto, BindingResult bindingResult, Model model) {

        if (!StringUtils.hasText(socialUserWithdrawRequestDto.getRequestEmail())) {
            bindingResult.rejectValue("requestEmail", "required", "");
        } else {
            if (!Pattern.matches("^(.+)@(.+)$", socialUserWithdrawRequestDto.getRequestEmail())) {
                bindingResult.addError(new FieldError("socialUserWithdrawRequestDto", "requestEmail", socialUserWithdrawRequestDto.getRequestEmail(), false, null, null, "이메일 형식에 맞지 않습니다."));
            } else if (!socialUserWithdrawRequestDto.getEmail().equals(socialUserWithdrawRequestDto.getRequestEmail())) {
                bindingResult.addError(new FieldError("socialUserWithdrawRequestDto", "requestEmail", socialUserWithdrawRequestDto.getRequestEmail(), false, null, null, "이메일이 일치하지 않습니다."));
            }
        }

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

        if (currentUserProfileImageId != null) {
            fileStoreService.deleteStoredFile(currentUserProfileImageId);
            settingService.deleteProfileImage(currentUserProfileImageId);
        }

        settingService.deleteByEmailAndProvider(socialUserWithdrawRequestDto.getEmail(), socialUserWithdrawRequestDto.getProvider());

        MessageDto message = new MessageDto("회원 탈퇴가 완료되었습니다.", "/logout", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    public boolean isCertifiedUser() {
        UserSignUpRequestDto userSignUpRequestDto = userService.getEmailCertifiedInfo();
        String emailCertified = userSignUpRequestDto.getCertified();

        if (!emailCertified.equals("Y")) {
            return false;
        }

        return true;
    }
}
