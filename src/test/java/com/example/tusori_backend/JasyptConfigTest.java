package com.example.tusori_backend;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {
    @Test
    @DisplayName("Jasypt 암호화 동작 테스트")
    void jasypt(){
        String url = "da98f42533532bd4a40530e1322c3635";
        String username = "86389da9306236c468c6cd0e13e194544bd5e91d5975d32bd3a71e4a845dddc39aba1e7014ff278c9d2d5be662a9f7bf0f25525de19937d8fa850a0036c54e75";
        String password = "18000000";

        String encryptUrl = jasyptEncrypt(url);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword : " + encryptPassword);

        Assertions.assertThat(url).isEqualTo(jasyptDecrypt(encryptUrl));
    }

    private String jasyptEncrypt(String input) {
        String key = "2024";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecrypt(String input){
        String key = "2024";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }
}
