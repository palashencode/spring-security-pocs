package com.java.app.usermgmt.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {
    private CryptoUtils(){}

    public static void main(String[] args) {
        System.out.println(resetPasswordToken());
        System.out.println(authenticityToken());
    }

    public static String emailVerificationToken() {
        return generateRandomTokenToken(64);
    }

    public static String getLoginMagicLink() {
        return generateRandomTokenToken(64);
    }

    public static String resetPasswordToken() {
        return generateRandomTokenToken(64);
    }

    public static String authenticityToken() {
        return generateRandomTokenToken(32);
    }

    private static String generateRandomTokenToken(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
