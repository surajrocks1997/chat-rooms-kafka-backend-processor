package com.example.chatroomskafkabackendprocessor.converter;

import com.example.chatroomskafkabackendprocessor.pojo.Message;
import org.springframework.kafka.support.serializer.JsonSerde;

public class ChatMessageSerDes extends JsonSerde<Message> {
    public ChatMessageSerDes() {
        super();
        ignoreTypeHeaders();
    }
}
