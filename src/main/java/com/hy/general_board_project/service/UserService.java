package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SettingService settingService;

    @Transactional
    public Long joinUser(UserSignUpRequestDto userSignUpRequestDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userSignUpRequestDto.setPassword(bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword()));

        userSignUpRequestDto.setRole(Role.USER);

        return userRepository.save(userSignUpRequestDto.toEntity()).getId();
    }

    @Transactional
    public User emailCertifiedCheck(UserSignUpRequestDto userSignUpRequestDto) {
        Optional<User> user = userRepository.findByEmailAndCertified(userSignUpRequestDto.getEmail(), userSignUpRequestDto.getCertified());

        return user.get();
    }

    @Transactional
    public void emailCertifiedUpdate(UserSignUpRequestDto userSignUpRequestDto) {
        User user = userRepository.findByEmailAndCertified(userSignUpRequestDto.getEmail(), userSignUpRequestDto.getCertified()).get();

        user.updateCertified();
    }

    public UserSignUpRequestDto getEmailCertifiedInfo() {
        UserDetails userDetails = settingService.getUserDetailsForFormUser();
        if (userDetails == null) {
            return null;
        }

        User user = settingService.getUserByUserDetailsForFormUser(userDetails).get();

        return new UserSignUpRequestDto(user.getRealName(), user.getUsername(), user.getNickname(), user.getPassword(), user.getEmail(), user.getRole(), user.getCertified());
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId).get();

        user.updateProfileImage(null);
    }

    public boolean isCertifiedUser() {
        UserSignUpRequestDto userSignUpRequestDto = getEmailCertifiedInfo();
        String emailCertified = userSignUpRequestDto.getCertified();

        if (!emailCertified.equals("Y")) {
            return false;
        }

        return true;
    }
}