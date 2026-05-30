package com.example.cinebook.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    private PasswordUtil() {
    }

    public static String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String rawPassword, String hash) {
        return BCrypt.checkpw(rawPassword, hash);
    }
}

