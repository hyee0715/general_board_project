package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FormUserWithdrawRequestDto implements UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String requestPassword;

    @Builder
    public FormUserWithdrawRequestDto(String username, String nickname, String requestPassword) {
        this.username = username;
        this.nickname = nickname;
        this.requestPassword = requestPassword;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .build();
    }
}
