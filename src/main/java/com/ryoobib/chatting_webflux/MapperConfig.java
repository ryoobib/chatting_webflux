package com.ryoobib.chatting_webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

@Configuration
@RequiredArgsConstructor
public class MapperConfig {
  @Bean
  public DataBufferFactory dataBufferFactory() {
    return new DefaultDataBufferFactory();
  }

  @Bean
  public Jackson2JsonEncoder jackson2JsonEncoder() {
    return new Jackson2JsonEncoder();
  }

  @Bean
  public Jackson2JsonDecoder jackson2JsonDecoder() {
    return new Jackson2JsonDecoder();
  }
}
