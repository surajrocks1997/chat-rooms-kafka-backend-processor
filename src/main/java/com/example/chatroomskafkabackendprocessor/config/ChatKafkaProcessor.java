package com.example.chatroomskafkabackendprocessor.config;

import com.example.chatroomskafkabackendprocessor.pojo.ChatRoomMessage;
import com.example.chatroomskafkabackendprocessor.pojo.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.function.Function;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ChatKafkaProcessor {

    @Bean
    public Function<KStream<String, ChatRoomMessage>, KStream<String, Long>> aggregateMessagesPerChatRoom() {
        return kStream -> kStream
                .filter(((key, value) -> value.getMessageType() == MessageType.CHAT_MESSAGE))
                .groupBy((key, value) -> value.getChatRoomName().toString(), Grouped.with(Serdes.String(), new JsonSerde<>(ChatRoomMessage.class)))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1)))
                .aggregate(() -> 0L,
                        (key, value, aggregate) -> aggregate + 1,
                        Materialized.with(Serdes.String(), Serdes.Long()))
//                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), value))
                .peek((key, value) -> log.info("Aggregated Messages Per Chat Room: Key: {}   -   Value: {}", key, value));
    }
}
