package com.example.distance.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void restTemplateBeanTest() {
        // Создаем контекст приложения с конфигурацией AppConfig
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Проверяем, что бин RestTemplate создан и доступен
        assertTrue(context.containsBean("restTemplate"));
        assertNotNull(context.getBean("restTemplate"));

        // Получаем бин RestTemplate
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        // Проверяем, что полученный бин является экземпляром RestTemplate
        assertTrue(restTemplate instanceof RestTemplate);

        // Закрываем контекст приложения
        context.close();
    }
}