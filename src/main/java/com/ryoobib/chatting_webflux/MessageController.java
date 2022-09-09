package com.ryoobib.chatting_webflux;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageController {

  private final MessageService messageService;

  @MessageMapping("join.room.{roomId}")
  public Flux<MessageRequest> join(@DestinationVariable String roomId) {
    return messageService.join(roomId);
  }

  @MessageMapping("create.room")
  public Mono<Void> createRoom(@Payload CreateRoomRequest request) {
    return messageService.createRoom(request);
  }

  @MessageMapping("show.room")
  public Flux<MessageRoomResponse> getRooms() {
    return messageService.getRooms();
  }

  @MessageMapping("{roomId}.send")
  public Mono<Void> sendMessage(@Payload MessageRequest message, @DestinationVariable String roomId) {
    return messageService.sendMessage(message, roomId);
  }
}
