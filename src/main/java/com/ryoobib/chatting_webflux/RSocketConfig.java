package com.ryoobib.chatting_webflux;

import io.rsocket.transport.netty.client.WebsocketClientTransport;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;

@Configuration
public class RSocketConfig {

  @Bean
  public RSocketRequester rSocketRequester() {
    RSocketStrategies rSocketStrategies = RSocketStrategies.builder()
        .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
        .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
        .build();

    return RSocketRequester.builder()
        .rsocketStrategies(rSocketStrategies)
        .transport(WebsocketClientTransport.create(URI.create("ws://localhost:7000/rs")));
  }

}
