package com.example.distance.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperConfigTest {

    @Test
    void modelMapperBeanTest() {
        // Создаем контекст приложения с конфигурацией ModelMapperConfig
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ModelMapperConfig.class);

        // Проверяем, что бин ModelMapper создан и доступен
        assertTrue(context.containsBean("modelMapper"));
        assertNotNull(context.getBean("modelMapper"));

        // Получаем бин ModelMapper
        ModelMapper modelMapper = context.getBean(ModelMapper.class);

        // Проверяем, что полученный бин является экземпляром ModelMapper
        assertTrue(modelMapper instanceof ModelMapper);

        // Дополнительные проверки конфигурации ModelMapper (при необходимости)
        // ...

        // Закрываем контекст приложения
        context.close();
    }
}