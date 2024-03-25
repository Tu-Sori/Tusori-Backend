package com.example.tusori_backend.service;

import com.example.tusori_backend.client.KakaoClient;
import com.example.tusori_backend.domain.dto.response.LoginResponse;
import com.example.tusori_backend.domain.entity.User;
import com.example.tusori_backend.jwt.JwtAuthenticationProvider;
import com.example.tusori_backend.params.KakaoInfoResponse;
import com.example.tusori_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public LoginResponse loginKakao(String authorizationCode) {
        String accessToken = kakaoClient.requestAccessToken(authorizationCode);
        KakaoInfoResponse info = kakaoClient.requestKakaoInfo(accessToken);

        Long userEmail = findOrCreateMember(info);
        User user = userRepository.findByEmail(userEmail).get();

        LoginResponse response = LoginResponse.builder()
                .id(user.getUserId())
                .nickname(user.getNickname())
                .accessToken(jwtAuthenticationProvider.createAccessToken(user.getUserId(), info.getProperties().getNickname()))
                .build();

        return response;
    }

    private Long findOrCreateMember(KakaoInfoResponse info) {
        return userRepository.findByEmail(info.getId())
                .map(User::getEmail)
                .orElseGet(() -> newMember(info));
    }

    private Long newMember(KakaoInfoResponse info) {
        User user = User.builder()
                .nickname(info.getProperties().getNickname())
                .email(info.getId())
                .assets(10000000)
                .build();

        userRepository.save(user);

        return user.getEmail();
    }
}
