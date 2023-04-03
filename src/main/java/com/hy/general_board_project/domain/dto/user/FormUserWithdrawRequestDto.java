package com.hy.general_board_project.domain.dto.user;

import com.hy.general_board_project.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FormUserWithdrawRequestDto implements UserDto {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
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
