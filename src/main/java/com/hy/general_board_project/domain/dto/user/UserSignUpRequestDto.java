package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserSignUpRequestDto implements UserDto {

    @Size(min = 2, max = 6, message = "이름은 2~6자리 제한입니다.", groups = RealNameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = RealNameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "이름은 한글로 구성되어야 합니다.", groups = RealNameValidationGroups.PatternCheckGroup.class)
    private String realName;

    @Size(min = 4, max = 20, message = "아이디는 4~20자리 제한입니다.", groups = UsernameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = UsernameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^([a-z0-9]){4,20}$", message = "아이디는 영어 소문자, 숫자로 구성되어야 합니다.", groups = UsernameValidationGroups.PatternCheckGroup.class)
    private String username;

    @Size(min = 2, max = 10, message = "닉네임은 2~10자리 제한입니다.", groups = NicknameValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = NicknameValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자가 포함될 수 없습니다.", groups = NicknameValidationGroups.PatternCheckGroup.class)
    private String nickname;

    @Size(min = 8, max = 20, message = "비밀번호는 8~10자리 제한입니다.", groups = PasswordValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = PasswordValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상으로 구성되어야 합니다.", groups = PasswordValidationGroups.PatternCheckGroup.class)
    private String password;

    @Email(groups = EmailValidationGroups.EmailCheckGroup.class)
    @NotBlank(groups = EmailValidationGroups.NotNullGroup.class)
    private String email;

    private Role role;

    private String certified;

    @Builder
    public UserSignUpRequestDto(String realName, String username, String nickname, String password, String email, Role role, String certified) {
        this.realName = realName;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.certified = certified;
    }

    public User toEntity() {
        return User.builder()
                .realName(realName)
                .username(username)
                .nickname(nickname)
                .password(password)
                .email(email)
                .role(role)
                .certified(certified)
                .build();
    }
}
