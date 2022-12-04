package com.hy.general_board_project.config.auth.dto;

import com.hy.general_board_project.domain.user.Role;
import com.hy.general_board_project.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Random;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String provider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name, String email, String picture, String provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
    }

    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(provider)) {
            return ofNaver("id", attributes, provider);
        }

        return ofGoogle(userNameAttributeName, attributes, provider);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String provider) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .provider(provider)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, String provider) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .provider(provider)
                .build();
    }

    public User toEntity() {
        int newNickname = new Random().nextInt() % 10000;

        return User.builder()
                .username(name)
                .email(email)
                .nickname("user_" + (Integer.toString(newNickname < 0 ? newNickname * - 1 : newNickname)))
                .picture(picture)
                .role(Role.USER)
                .provider(provider)
                .build();
    }
}