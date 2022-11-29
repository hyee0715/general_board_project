package com.hy.general_board_project.config.auth;

import com.hy.general_board_project.config.auth.dto.OAuthAttributes;
import com.hy.general_board_project.config.auth.dto.SessionUser;
import com.hy.general_board_project.domain.user.SocialUser;
import com.hy.general_board_project.domain.user.SocialUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final SocialUserRepository socialUserRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        SocialUser socialUser = saveOrUpdate(attributes);
        httpSession.setAttribute("socialUser", new SessionUser(socialUser));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(socialUser.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private SocialUser saveOrUpdate(OAuthAttributes attributes) {
        SocialUser socialUser = socialUserRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture())).orElse(attributes.toEntity());

        return socialUserRepository.save(socialUser);
    }
}