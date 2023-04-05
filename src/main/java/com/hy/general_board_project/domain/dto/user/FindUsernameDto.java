package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.user.EmailValidationGroups;
import com.hy.general_board_project.validator.validation.user.FindUsername;
import com.hy.general_board_project.validator.validation.user.FindUsernameValidationGroups;
import com.hy.general_board_project.validator.validation.user.RealNameValidationGroups;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FindUsername(groups = FindUsernameValidationGroups.FindUsernameCheckGroup.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FindUsernameDto implements UserDto {

    @Size(min = 2, max = 6, message = "이름은 2~6자리 제한입니다.", groups = RealNameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = RealNameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "이름은 한글로 구성되어야 합니다.", groups = RealNameValidationGroups.PatternCheckGroup.class)
    private String realName;

    private String username;

    @Email(groups = EmailValidationGroups.EmailCheckGroup.class)
    @NotBlank(groups = EmailValidationGroups.NotNullGroup.class)
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
