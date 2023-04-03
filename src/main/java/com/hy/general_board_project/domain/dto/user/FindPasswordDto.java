package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FindPasswordDto implements UserDto {

    @NotBlank
    private String realName;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    private String tempPassword;

    @Builder
    public FindPasswordDto(String realName, String username, String email, String tempPassword) {
        this.realName = realName;
        this.username = username;
        this.email = email;
        this.tempPassword = tempPassword;
    }

    public User toEntity() {
        return User.builder()
                .realName(realName)
                .username(username)
                .email(email)
                .build();
    }
}
