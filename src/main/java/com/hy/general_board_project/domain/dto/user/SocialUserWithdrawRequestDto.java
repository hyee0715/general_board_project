package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import com.hy.general_board_project.validator.validation.setting.WithdrawEmail;
import com.hy.general_board_project.validator.validation.setting.WithdrawEmailValidationGroups;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@WithdrawEmail(groups = WithdrawEmailValidationGroups.IdentificationCheckWithEmailGroup.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SocialUserWithdrawRequestDto implements UserDto {

    private Long id;

    @NotBlank
    private String email;

    private String provider;

    @Email(groups = WithdrawEmailValidationGroups.EmailCheckGroup.class)
    @NotBlank(groups = WithdrawEmailValidationGroups.NotNullGroup.class)
    private String requestEmail;

    @Builder
    public SocialUserWithdrawRequestDto(Long id, String email, String provider, String requestEmail) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.requestEmail = requestEmail;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .provider(provider)
                .build();
    }

    public boolean isValidWithdrawEmail() {
        return email.equals(requestEmail);
    }
}
