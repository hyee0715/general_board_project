package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequestDto {

    private String username;
    private String nickname;
    private String password;
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
