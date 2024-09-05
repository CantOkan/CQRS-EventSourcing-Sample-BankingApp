package com.bank.cqrs.core.producers;

import com.bank.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void send(String topic, BaseEvent event);
}
