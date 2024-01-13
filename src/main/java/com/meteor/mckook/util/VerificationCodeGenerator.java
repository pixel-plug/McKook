package com.meteor.mckook.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 验证码生成工具
 */
public class VerificationCodeGenerator {
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;
    private static final Random random = new SecureRandom();

    public static String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            code.append(CHAR_SET.charAt(index));
        }

        return code.toString();
    }
}