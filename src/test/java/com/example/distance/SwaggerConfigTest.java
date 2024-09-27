package com.example.distance;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.junit.jupiter.api.Assertions.*;

@EnableSwagger2 // Необходимо для активации Swagger
class SwaggerConfigTest {

    @Test
    void apiBeanTest() {
        // Создаем контекст приложения с конфигурацией SwaggerConfig
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SwaggerConfig.class);

        // Проверяем, что бин Docket создан и доступен
        assertTrue(context.containsBean("api"));
        assertNotNull(context.getBean("api"));

        // Получаем бин Docket
        Docket docket = context.getBean(Docket.class);

        // Проверяем, что полученный бин является экземпляром Docket
        assertTrue(docket instanceof Docket);

        // Закрываем контекст приложения
        context.close();
    }
}