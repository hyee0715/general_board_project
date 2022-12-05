package com.hy.general_board_project.web.controller;

import com.hy.general_board_project.service.UserService;
import com.hy.general_board_project.web.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user/signUp")
    public Long signUp(@RequestBody UserSignUpRequestDto requestDto) {
        return userService.joinUser(requestDto);
    }
}
