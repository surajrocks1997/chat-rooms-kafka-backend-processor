package com.example.chatroomskafkabackendprocessor.config;

import com.example.chatroomskafkabackendprocessor.pojo.Message;
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
    public Function<KStream<String, Message>, KStream<String, String>> aggregate() {
        return kStream -> kStream
                .groupBy((key, value) -> value.getChatRoomName(), Grouped.with(Serdes.String(), new JsonSerde<>(Message.class)))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(30)))
                .aggregate(() -> 0L,
                        (key, value, aggregate) -> aggregate + 1,
                        Materialized.with(Serdes.String(), Serdes.Long()))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map((key, value) -> new KeyValue<>(key.key(), value.toString()))
                .peek((key, value) -> log.info("Aggregated Data: Key: {}   -   Value: {}", key, value));
    }
}
