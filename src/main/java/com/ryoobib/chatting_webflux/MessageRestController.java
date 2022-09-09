package com.ryoobib.chatting_webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Controller
@RestController
@RequiredArgsConstructor
public class MessageRestController {

  private final RSocketRequester requester;
  private final MessageRoomRepository messageRoomRepository;


  @GetMapping(value = "/room", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<MessageRoomResponse> getRooms() {
    return requester.route("show.room").retrieveFlux(MessageRoomResponse.class);
  }

  @PostMapping(value = "/room")
  public Mono<Void> createRoom(CreateRoomRequest request) {
    return requester.route("create.room").data(request).retrieveMono(Void.class);
  }

  @PostMapping(value = "/room/{roomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<MessageRequest> join(@PathVariable String roomId) {
    return requester.route("join.room."+roomId).retrieveFlux(MessageRequest.class);
  }

  @PostMapping(value = "/message/{roomId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<Void> send(@PathVariable String roomId, MessageRequest messageRequest) {
    return requester.route(roomId+".send").data(messageRequest).retrieveMono(Void.class);
  }

}
