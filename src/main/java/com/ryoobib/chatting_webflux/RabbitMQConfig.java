package com.ryoobib.chatting_webflux;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import javax.annotation.PreDestroy;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE_NAME = "chat.room.exchange";


  @Bean
  Mono<Connection> connectionMono() {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.useNio();
    connectionFactory.setUsername("test");
    connectionFactory.setPassword("test");
    return Mono.fromCallable(connectionFactory::newConnection).cache();
  }

//  @Bean
//  public Mono<Connection> connectionMono(CachingConnectionFactory connectionFactory) {
//    return Mono.fromCallable(() -> connectionFactory.getRabbitConnectionFactory().newConnection());
//  }

  @Bean
  public Sender sender(Mono<Connection> mono) {
    return RabbitFlux.createSender(new SenderOptions().connectionMono(mono));           // RabbitConnection으로 Sender 생성
  }

  @Bean
  public Receiver receiver(Mono<Connection> connectionMono) {
    return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono)); // Receiver 생성
  }

}
