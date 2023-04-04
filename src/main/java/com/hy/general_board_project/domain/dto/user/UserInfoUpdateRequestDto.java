package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.signUp.NicknameValidationGroups;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    @Size(min = 2, max = 10, message = "닉네임은 2~10자리 제한입니다.", groups = NicknameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = NicknameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자가 포함될 수 없습니다.", groups = NicknameValidationGroups.PatternCheckGroup.class)
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
