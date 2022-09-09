package com.ryoobib.chatting_webflux;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRoomResponse {

  private String roomName;

  private String roomId;

  private String lastMessage;

  private boolean isRead;
}
