package com.hy.general_board_project.service;

import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserInfoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    public String updateUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUser(userInfoUpdateRequestDto);

        user.updateNickname(userInfoUpdateRequestDto.getNickname());

        return userInfoUpdateRequestDto.getNickname();
    }

    public User findUser(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        Optional<User> user = userRepository.findByEmailAndProvider(userInfoUpdateRequestDto.getEmail(), userInfoUpdateRequestDto.getProvider());

        if (userInfoUpdateRequestDto.getProvider().isEmpty()) {
            user = userRepository.findByUsername(userInfoUpdateRequestDto.getUsername());
        }

        return user.get();
    }

    public String findUserNickname(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        return findUser(userInfoUpdateRequestDto).getNickname();
    }

    public UserInfoUpdateRequestDto findUserInfo() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser != null) {
            Optional<User> userEntity = userRepository.findByEmailAndProvider(sessionUser.getEmail(), sessionUser.getProvider());

            User user = userEntity.get();

            return new UserInfoUpdateRequestDto(user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider());
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String anonymousUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (anonymousUserName.equals("anonymousUser")) {
            return null;
        }

        UserDetails userDetails = (UserDetails)principal;
        String username = userDetails.getUsername();
        Optional<User> userEntity = userRepository.findByUsername(username);

        User user = userEntity.get();

        return new UserInfoUpdateRequestDto(user.getUsername(), user.getNickname(), user.getEmail(), user.getProvider());
    }
}
