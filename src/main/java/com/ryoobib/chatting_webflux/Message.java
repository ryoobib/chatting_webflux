package com.ryoobib.chatting_webflux;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private String message; // Hold a message
  private long created = Instant.now().getEpochSecond(); // Something instance specific

  /**
   * Allow creation from a message.
   * @param message
   */
  public Message(String message) {
    this.message = message;
  }
}