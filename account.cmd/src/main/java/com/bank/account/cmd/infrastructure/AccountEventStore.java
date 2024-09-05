package com.bank.account.cmd.infrastructure;

import com.bank.account.cmd.domain.AccountAggregate;
import com.bank.account.cmd.domain.EventStoreRepository;
import com.bank.cqrs.core.events.BaseEvent;
import com.bank.cqrs.core.events.EventModel;
import com.bank.cqrs.core.infrastructure.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;
    private final AccountEventProducer accountEventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventModels = eventStoreRepository.findByAggregateId(aggregateId);

        if (expectedVersion != -1 && eventModels.get(eventModels.size() - 1).getVersion() != expectedVersion) {
            throw new RuntimeException("Concurrency exception");
        }

        int version = expectedVersion;
        for (var event : events) {
            event.setVersion(version++);

            EventModel eventModel = EventModel.builder()
                    .aggregateId(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .eventType(event.getClass().getName())
                    .eventData(event)
                    .version(event.getVersion())
                    .timestamp(new Date())
                    .build();

            EventModel savedEvent = eventStoreRepository.save(eventModel);
            if (savedEvent.getId() != null && !savedEvent.getId().isEmpty()) {
                accountEventProducer.send(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> findByAggregateId(String aggregateId) {
        List<EventModel> eventModels = eventStoreRepository.findByAggregateId(aggregateId);
        if (eventModels == null || eventModels.isEmpty()) {
            throw new RuntimeException("Aggregate not found");
        }
        return eventModels.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }

    @Override
    public List<String> findAllAggregateIds() {
        List<EventModel> eventStream = eventStoreRepository.findAll();
        if (eventStream == null || eventStream.isEmpty()) {
            throw new IllegalStateException("No events found");
        }
        return eventStream.stream().map(EventModel::getAggregateId).distinct().collect(Collectors.toList());
    }
}
