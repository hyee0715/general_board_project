package com.hy.general_board_project.web.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInfoUpdateRequestDto implements UserDto {

    @NotBlank
    private String realName;

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @Email
    @NotBlank
    private String email;

    private String provider;

    @Builder
    public UserInfoUpdateRequestDto(String realName, String username, String nickname, String email, String provider) {
        this.realName = realName;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
    }

    public User toEntity() {
        return User.builder()
                .realName(realName)
                .username(username)
                .nickname(nickname)
                .email(email)
                .provider(provider)
                .build();
    }

    public UserPasswordUpdateRequestDto convertToPasswordUpdateDto() {
        return UserPasswordUpdateRequestDto.builder()
                .username(username)
                .currentPassword(null)
                .newPassword(null)
                .newPasswordConfirm(null)
                .build();
    }
}
