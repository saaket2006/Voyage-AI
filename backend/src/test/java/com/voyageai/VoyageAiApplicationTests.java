package com.voyageai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VoyageAiApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring application context boots successfully with the test profile.
    }
}
