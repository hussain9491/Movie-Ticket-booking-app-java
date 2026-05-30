package com.example.cinebook.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FxmlLoadTest {

    private static final AtomicBoolean FX_STARTED = new AtomicBoolean(false);

    @BeforeAll
    static void startJavaFx() throws InterruptedException {
        if (FX_STARTED.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
    }

    @Test
    void adminScreenLoads() {
        assertDoesNotThrow(() -> load("/com/example/cinebook/view/admin.fxml"));
    }

    @Test
    void bookingsScreenLoads() {
        assertDoesNotThrow(() -> load("/com/example/cinebook/view/bookings.fxml"));
    }

    private static void load(String path) throws Exception {
        URL url = FxmlLoadTest.class.getResource(path);
        assertNotNull(url, "Missing resource: " + path);
        new FXMLLoader(url).load();
    }
}

