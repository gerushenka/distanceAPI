package com.example.distance.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация ModelMapper.
 */
@Configuration
public class ModelMapperConfig {

  /**
   * Создает и возвращает экземпляр ModelMapper.
   *
   * @return экземпляр ModelMapper
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}