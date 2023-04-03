package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInfoUpdateRequestDto implements UserDto {

    private Long id;

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

    private MultipartFile profileImage;

    private Long profileImageId;

    @Builder
    public UserInfoUpdateRequestDto(Long id, String realName, String username, String nickname, String email, String provider, Long profileImageId) {
        this.id = id;
        this.realName = realName;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.profileImageId = profileImageId;
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
