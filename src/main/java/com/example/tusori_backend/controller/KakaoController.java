//package com.example.tusori_backend.controller;
//
//import org.springframework.http.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//@Controller
//@RequestMapping("/oauth2/kakao")
//public class KakaoController {
//
//    @GetMapping
//    public String getAccessToken(@RequestParam("code") String code) {
//        System.out.println("code: " + code);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "da98f42533532bd4a40530e1322c3635");
//        params.add("client_id", "REST_API_KEY 입력");
//        params.add("redirect_uri", "http://localhost:8080/oauth2/kakao");
//        params.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Object> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/toekn",
//                HttpMethod.POST,
//                httpEntity,
//                Object.class
//        );
//
//        System.out.println("response: " + response);
//
//        return "home";
//    }
//
//}
