package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.setting.WithdrawPassword;
import com.hy.general_board_project.validator.validation.setting.WithdrawPasswordValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FormUserWithdrawRequestDto implements UserDto {

    private Long id;

    @NotBlank
    private String username;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리 제한입니다.", groups = WithdrawPasswordValidationGroups.SizeCheckGroup.class)
    @NotBlank(groups = WithdrawPasswordValidationGroups.NotNullGroup.class)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문 대소문자, 숫자, 특수기호 1개 이상으로 구성되어야 합니다.", groups = WithdrawPasswordValidationGroups.PatternCheckGroup.class)
    @WithdrawPassword(groups = WithdrawPasswordValidationGroups.IdentificationCheckWithPasswordGroup.class)
    private String requestPassword;

    @Builder
    public FormUserWithdrawRequestDto(Long id, String username, String requestPassword) {
        this.id = id;
        this.username = username;
        this.requestPassword = requestPassword;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .build();
    }
}
