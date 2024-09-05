package com.bank.account.cmd.infrastructure;

import com.bank.cqrs.core.events.BaseEvent;
import com.bank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void send(String topic, BaseEvent event) {
        kafkaTemplate.send(topic, event);
    }

}
