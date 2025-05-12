package com.routinenyang.backend.auth.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.routinenyang.backend.user.entity.User;
import com.routinenyang.backend.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserDetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("Attributes: {}", oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet( () -> {
                    User newUser = User.builder()
                            .email(oAuth2UserInfo.getEmail())
                            .name(oAuth2UserInfo.getName())
                            .onBoardingFinished(false)
                            .build();
                    return userRepository.save(newUser);
                });
        return new CustomUserDetails(user);
    }
}
