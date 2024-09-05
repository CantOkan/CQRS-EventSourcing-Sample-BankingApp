package com.bank.cqrs.core.infrastructure;

import com.bank.cqrs.core.events.BaseEvent;
import com.bank.cqrs.core.events.EventModel;

import java.util.List;

public interface EventStore {
    void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);

    List<BaseEvent> findByAggregateId(String aggregateId);

    List<String> findAllAggregateIds();
}
