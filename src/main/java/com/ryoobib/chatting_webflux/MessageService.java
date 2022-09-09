package com.ryoobib.chatting_webflux;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.BindingSpecification;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

@RequiredArgsConstructor
@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final MessageRoomRepository messageRoomRepository;
  private final Receiver receiver;
  private final Sender sender;
  private final ReactiveObjectMapper objectMapper;
  //  private final String roomId = "chat";
  private final String author = "sender";

  private final String topic = "chat.room.exchange";

  Logger logger = LoggerFactory.getLogger(MessageService.class);

  public Flux<MessageRequest> join(String roomId) {
    return joinRoom(roomId)
        .thenMany(getMessageFromQueue(roomId));
  }

  private Mono<Void> joinRoom(String roomId) {
    return sender
        .declareQueue(QueueSpecification.queue(roomId + "." + author)
            .autoDelete(false))
        .flatMap(declareOk -> sender
            .bindQueue(BindingSpecification
            .binding()
            .routingKey(roomId)
            .queue(roomId + "." + author)
            .exchangeFrom(roomId))
            .map(bindOk -> declareOk.getQueue()))
        .then();
  }

  private Flux<MessageRequest> getMessageFromQueue(String roomId) {
  logger.warn("R U Here ?? in getMessageFromQueue()");
    return receiver.consumeAutoAck(roomId + "." + author)
        .map(delivery -> new MessageRequest(new String(delivery.getBody()), author));
  }

  public Mono<Void> createRoom(CreateRoomRequest request) {
    return messageRoomRepository
        .save(MessageRoom.builder()  // DB에 새로운 채팅룸 저장
            .members(List.of("test"))
            .roomName(request.getRoomName())
            .build())
        .flatMap(messageRoom -> sender // 메세지큐에 Exchange 등록
            .declareExchange(ExchangeSpecification
                .exchange(RabbitMQConfig.EXCHANGE_NAME)
                .type("direct"))
            .then(Mono.just(messageRoom)))
        .flatMap(messageRoom -> sender
            .declareExchange(ExchangeSpecification
                .exchange()
                .name(messageRoom.getId())
                .type("fanout"))
            .then(Mono.just(messageRoom)))
        .flatMap(messageRoom -> sender // 메세지큐에 Exchange 경로 등록
            .bindExchange(BindingSpecification
                .binding()
                .routingKey(messageRoom.getId())
                .exchangeTo(messageRoom.getId())
                .exchangeFrom(RabbitMQConfig.EXCHANGE_NAME))
            .then(Mono.just(messageRoom)))
        .flatMap(messageRoom -> joinRoom(messageRoom.getId())) // 생성된 룸에 유저 입장
        .then();

  }

  public Flux<MessageRoomResponse> getRooms() {
    return null;
  }

  public Mono<Void> sendMessage(MessageRequest message, String roomId) {
    return objectMapper.encodeValue(MessageRequest.class, Mono.just(message))
        .map(buffer -> Mono.just(new OutboundMessage(RabbitMQConfig.EXCHANGE_NAME, roomId,
            buffer.asByteBuffer().array())))
        .flatMap(sender::send)
        .flatMap(unused -> messageRepository.save(toEntity(message.getMessage(), roomId)))
        .then();
  }

  private Message toEntity(String message, String roomId) {
    System.out.println("R U here??       " +  message);
    return Message.builder()
        .roomId(roomId)
        .message(message)
        .author(author)
        .build();
  }
}
