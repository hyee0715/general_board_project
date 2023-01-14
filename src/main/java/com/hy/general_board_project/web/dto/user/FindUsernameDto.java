package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FindUsernameDto implements UserDto {

    @NotBlank
    private String realName;

    private String username;

    @NotBlank
    private String email;

    @Builder
    public FindUsernameDto(String realName, String username, String email) {
        this.realName = realName;
        this.username = username;
        this.email = email;
    }

    public User toEntity() {
        return User.builder()
                .realName(realName)
                .username(username)
                .email(email)
                .build();
    }
}
