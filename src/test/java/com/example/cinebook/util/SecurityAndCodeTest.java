package com.example.cinebook.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityAndCodeTest {

    @Test
    void passwordHashAndVerifyWork() {
        String raw = "secret123";
        String hash = PasswordUtil.hashPassword(raw);

        assertNotEquals(raw, hash);
        assertTrue(PasswordUtil.verifyPassword(raw, hash));
    }

    @Test
    void bookingCodeMatchesExpectedPattern() {
        String code = BookingCodeGenerator.nextCode();
        assertTrue(code.matches("CB-\\d{8}-\\d{4}"));
    }
}

