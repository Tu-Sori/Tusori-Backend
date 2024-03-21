package com.example.tusori_backend.client;

import com.example.tusori_backend.params.KakaoInfoResponse;
import com.example.tusori_backend.params.KakaoTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class KakaoClient {
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    // 카카오 서버에 access token 요청
    public String requestAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        String requestUri = "https://kauth.kakao.com/oauth/token";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        KakaoTokens response = restTemplate.postForObject(requestUri, request, KakaoTokens.class);

        return response.getAccessToken();
    }

    // 카카오 서버에 사용자 정보 요청
    public KakaoInfoResponse requestKakaoInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestUri = "https://kapi.kakao.com/v2/user/me";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        KakaoInfoResponse response = restTemplate.postForObject(requestUri, request, KakaoInfoResponse.class);

        return response;
    }
}
