package com.ryoobib.chatting_webflux;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class MessageRoom {
  @MongoId
  private String id;

  private String roomName;

  private List<String> members;
}
