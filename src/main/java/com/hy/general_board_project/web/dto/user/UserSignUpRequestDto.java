package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserSignUpRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    private Role role;

    @Builder
    public UserSignUpRequestDto(String username, String nickname, String password, String email, Role role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .email(email)
                .role(role)
                .build();
    }
}
