package com.hy.general_board_project.service;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.domain.user.UserRepository;
import com.hy.general_board_project.domain.dto.user.FindPasswordDto;
import com.hy.general_board_project.domain.dto.user.FindUsernameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserFindService {

    private final UserRepository userRepository;

    @Transactional
    public boolean existsUserByRealNameAndEmail(String realName, String email) {
        Optional<User> user = userRepository.findByEmailAndProvider(email, null);

        if (user.isEmpty() || !user.get().getRealName().equals(realName)) {
            return false;
        }

        return true;
    }

    @Transactional
    public FindUsernameDto getFindUsernameDtoByEmail(String email) {
        User user = userRepository.findByEmailAndProvider(email, null).get();

        return FindUsernameDto.builder()
                .realName(user.getRealName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public boolean existsUserByRealNameAndUsernameAndEmail(String realName, String username, String email) {
        Optional<User> user = userRepository.findByEmailAndProvider(email, null);

        if (user.isEmpty() || !user.get().getRealName().equals(realName) || !user.get().getUsername().equals(username)) {
            return false;
        }

        return true;
    }

    public String makeRandomCharacters() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        while (sb.length() < 18) {
            int num = random.nextInt(90) + 33;
            if ((num >= 33 && num <= 46) || (num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            }
        }

        return sb.toString();
    }

    public String makeTempPassword() {
        String tempPassword;

        do {
            tempPassword = makeRandomCharacters();
        } while (!validatePasswordFormat(tempPassword));

        return tempPassword;
    }

    public boolean validatePasswordFormat(String password) {
        Set<Character> set = new HashSet<>();

        for (char c : password.toCharArray()) {
            set.add(c);
        }

        return validatePasswordFormatContainsSpecialCharacter(set) && validatePasswordFormatContainsDigit(set) && validatePasswordFormatContainsLetters(set);
    }

    public boolean validatePasswordFormatContainsSpecialCharacter(Set<Character> set) {
        for (int i = 33; i <= 46; i++) {
            if (set.contains((char) i)) {
                return true;
            }
        }

        return false;
    }

    public boolean validatePasswordFormatContainsDigit(Set<Character> set) {
        for (int i = 48; i <= 57; i++) {
            if (set.contains((char) i)) {
                return true;
            }
        }

        return false;
    }

    public boolean validatePasswordFormatContainsUpperCaseLetters(Set<Character> set) {
        for (int i = 65; i <= 90; i++) {
            if (set.contains((char) i)) {
                return true;
            }
        }

        return false;
    }

    public boolean validatePasswordFormatContainsLowerCaseLetters(Set<Character> set) {
        for (int i = 97; i <= 122; i++) {
            if (set.contains((char) i)) {
                return true;
            }
        }

        return false;
    }

    public boolean validatePasswordFormatContainsLetters(Set<Character> set) {
        if (validatePasswordFormatContainsUpperCaseLetters(set) || validatePasswordFormatContainsLowerCaseLetters(set)) {
            return true;
        }

        return false;
    }

    @Transactional
    public User updateUserPasswordToTempPassword(FindPasswordDto findPasswordDto, String encodedTempPassword) {
        Optional<User> user = userRepository.findByEmailAndProvider(findPasswordDto.getEmail(), null)
                .map(entity -> entity.updatePassword(encodedTempPassword));

        return userRepository.save(user.get());
    }
}
