package com.ryoobib.chatting_webflux;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRoomRepository  extends ReactiveMongoRepository<MessageRoom, String> {
  Flux<MessageRoom> findAllByMembersContaining(String member);
}
