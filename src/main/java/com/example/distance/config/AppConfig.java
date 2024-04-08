package com.example.distance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация приложения.
 */
@Configuration
@ControllerAdvice
public class AppConfig {

  /**
   * Создает и возвращает новый экземпляр RestTemplate.
   *
   * @return новый экземпляр RestTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}