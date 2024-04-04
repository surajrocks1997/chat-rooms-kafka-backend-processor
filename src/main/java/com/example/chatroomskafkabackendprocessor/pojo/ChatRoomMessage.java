package com.example.chatroomskafkabackendprocessor.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomMessage {
    private MessageType messageType;
    private String username;
    private ChatRoomName chatRoomName;
    private String message;
    private String timestamp;
}
