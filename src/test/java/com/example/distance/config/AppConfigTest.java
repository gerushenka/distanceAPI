package com.example.distance.config;

import com.example.distance.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = AppConfig.class)
class AppConfigTest {

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testRestTemplateBean() {
        assertNotNull(restTemplate);
    }
}
