package com.example.tusori_backend.params;

import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoInfoResponse {
    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @ToString
    public static class KakaoAccount {
        private String email;
    }

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }
}
