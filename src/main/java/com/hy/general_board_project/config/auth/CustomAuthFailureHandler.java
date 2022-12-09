package com.hy.general_board_project.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /***
     * @param request request에 대한 정보
     * @param response response에 대해 설정할 수 있는 변수
     * @param exception 로그인 실패 시 예외 정보
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            if (request.getContentLength() == 19) { //아이디와 비밀번호 모두 입력하지 않았을 경우
                errorMessage = "아이디와 비밀번호를 입력해주세요.";
            } else { // 입력은 했으나 틀렸을 경우
                //errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
                errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.";
            }
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            errorMessage = "알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        setDefaultFailureUrl("/user/login?error=true&exception=" + errorMessage);

        log.info("exception = {}", exception.toString());
        log.info("request = {}", request.getContentLength());

        super.onAuthenticationFailure(request, response, exception);
    }
}