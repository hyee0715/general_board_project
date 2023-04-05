package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.setting.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NewPasswordConfirm(groups = NewPasswordConfirmValidationGroups.IdentificationCheckWithNewPasswordGroup.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserPasswordUpdateRequestDto implements UserDto {
    @NotBlank
    private String username;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리 제한입니다.", groups = CurrentPasswordValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = CurrentPasswordValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상으로 구성되어야 합니다.", groups = CurrentPasswordValidationGroups.PatternCheckGroup.class)
    @CurrentPassword(groups = CurrentPasswordValidationGroups.IdentificationCheckGroup.class)
    public String currentPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리 제한입니다.", groups = NewPasswordValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = NewPasswordValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상으로 구성되어야 합니다.", groups = NewPasswordValidationGroups.PatternCheckGroup.class)
    @NewPassword(groups = NewPasswordValidationGroups.IdentificationCheckWithCurrentPasswordGroup.class)
    private String newPassword;

    @NotBlank(groups = NewPasswordConfirmValidationGroups.NotNullGroup.class)
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

    public boolean isValidNewPasswordConfirm() {
        return newPassword.equals(newPasswordConfirm);
    }
}
