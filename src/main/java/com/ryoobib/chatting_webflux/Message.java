package com.ryoobib.chatting_webflux;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
  private String id;
  private String author;
  private String message; // Hold a message
  private long created = Instant.now().getEpochSecond(); // Something instance specific
  private String roomId;

  /**
   * Allow creation from a message.
   * @param message
   */
  public Message(String message) {
    this.message = message;
  }
}