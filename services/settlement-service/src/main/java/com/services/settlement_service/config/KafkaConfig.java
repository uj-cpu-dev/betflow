package com.services.settlement_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.services.settlement_service.event.BetPlacedEvent;
import com.services.settlement_service.event.BetSettledEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    // ── Consumer ─────────────────────────────────────────────────────────────

    @Bean
    public ConsumerFactory<String, BetPlacedEvent> consumerFactory(ObjectMapper kafkaObjectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        DefaultKafkaConsumerFactory<String, BetPlacedEvent> factory =
                new DefaultKafkaConsumerFactory<>(config);

        factory.setValueDeserializer(new org.apache.kafka.common.serialization.Deserializer<>() {
            @Override
            public BetPlacedEvent deserialize(String topic, byte[] data) {
                try {
                    return kafkaObjectMapper.readValue(data, BetPlacedEvent.class);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to deserialize BetPlacedEvent", e);
                }
            }
        });

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BetPlacedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, BetPlacedEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, BetPlacedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    // ── Producer ─────────────────────────────────────────────────────────────

    @Bean
    public ProducerFactory<String, BetSettledEvent> producerFactory(ObjectMapper kafkaObjectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.ByteArraySerializer.class);

        DefaultKafkaProducerFactory<String, BetSettledEvent> factory =
                new DefaultKafkaProducerFactory<>(config);

        factory.setValueSerializer(new org.apache.kafka.common.serialization.Serializer<>() {
            @Override
            public byte[] serialize(String topic, BetSettledEvent data) {
                try {
                    return kafkaObjectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to serialize BetSettledEvent", e);
                }
            }
        });

        return factory;
    }

    @Bean
    public KafkaTemplate<String, BetSettledEvent> kafkaTemplate(
            ProducerFactory<String, BetSettledEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
