package com.example.chatroomskafkabackendprocessor.converter;

import com.example.chatroomskafkabackendprocessor.pojo.ChatRoomMessage;
import org.springframework.kafka.support.serializer.JsonSerde;

public class ChatMessageSerDes extends JsonSerde<ChatRoomMessage> {
    public ChatMessageSerDes() {
        super();
        ignoreTypeHeaders();
    }
}
