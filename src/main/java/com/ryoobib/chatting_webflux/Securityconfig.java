package com.ryoobib.chatting_webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class Securityconfig {

  @Bean
  RSocketMessageHandler messageHandler() {
    RSocketStrategies strategies = RSocketStrategies.builder()
        .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
        .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
        .build();

    RSocketMessageHandler handler = new RSocketMessageHandler();
    handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
    handler.setRSocketStrategies(strategies);
    return handler;
  }

  @Bean
  MapReactiveUserDetailsService authentication() {
    //This is NOT intended for production use (it is intended for getting started experience only)
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("pass")
        .roles("USER")
        .build();

    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  PayloadSocketAcceptorInterceptor authorization(RSocketSecurity security) {
    security.authorizePayload(authorize ->
        authorize
            .anyExchange().authenticated() // all connections, exchanges.
    ).simpleAuthentication(Customizer.withDefaults());
    return security.build();
  }
}
