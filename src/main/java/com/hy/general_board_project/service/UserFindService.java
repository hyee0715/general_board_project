package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.web.dto.user.FindUsernameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserFindService {

    private final UserRepository userRepository;

    public boolean existsUserByRealNameAndEmail(String realName, String email) {
        Optional<User> user = userRepository.findByEmailAndProvider(email, null);

        if (user.isEmpty()) {
            return false;
        }

        if (!user.get().getRealName().equals(realName)) {
            return false;
        }

        return true;
    }

    public FindUsernameDto getFindUsernameDtoByEmail(String email) {
        User user = userRepository.findByEmailAndProvider(email, null).get();

        return FindUsernameDto.builder()
                .realName(user.getRealName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
