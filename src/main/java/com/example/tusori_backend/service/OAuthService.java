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

        Long userId = findOrCreateMember(info);
        User user = userRepository.findById(userId).get();

        LoginResponse response = LoginResponse.builder()
                .id(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accessToken(jwtAuthenticationProvider.createAccessToken(userId, info.getProperties().getNickname()))
                .build();

        return response;
    }

    private Long findOrCreateMember(KakaoInfoResponse info) {
        return userRepository.findByEmail(info.getKakao_account().getEmail())
                .map(User::getUserId)
                .orElseGet(() -> newMember(info));
    }

    private Long newMember(KakaoInfoResponse info) {
        User user = User.builder()
                .userId(info.getId())
                .nickname(info.getProperties().getNickname())
                .email(info.getKakao_account().getEmail())
                .build();

        userRepository.save(user);

        return user.getUserId();
    }
}
