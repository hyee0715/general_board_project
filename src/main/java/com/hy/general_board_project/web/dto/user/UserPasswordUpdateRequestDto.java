package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserPasswordUpdateRequestDto implements UserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;

    @Builder
    public UserPasswordUpdateRequestDto(String username, String currentPassword, String newPassword, String newPasswordConfirm) {
        this.username = username;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .build();
    }
}
