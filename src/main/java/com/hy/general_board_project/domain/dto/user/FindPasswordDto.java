package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.user.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FindPassword(groups = FindPasswordValidationGroups.FindPasswordCheckGroup.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FindPasswordDto implements UserDto {

    @Size(min = 2, max = 6, message = "이름은 2~6자리 제한입니다.", groups = RealNameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = RealNameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "이름은 한글로 구성되어야 합니다.", groups = RealNameValidationGroups.PatternCheckGroup.class)
    private String realName;

    @Size(min = 4, max = 20, message = "아이디는 4~20자리 제한입니다.", groups = UsernameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = UsernameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^([a-z0-9]){4,20}$", message = "아이디는 영어 소문자, 숫자로 구성되어야 합니다.", groups = UsernameValidationGroups.PatternCheckGroup.class)
    private String username;

    @Email(groups = EmailValidationGroups.EmailCheckGroup.class)
    @NotBlank(groups = EmailValidationGroups.NotNullGroup.class)
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
