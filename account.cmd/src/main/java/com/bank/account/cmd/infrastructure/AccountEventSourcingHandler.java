package com.bank.account.cmd.infrastructure;

import com.bank.account.cmd.domain.AccountAggregate;
import com.bank.cqrs.core.domian.AggregateRoot;
import com.bank.cqrs.core.events.BaseEvent;
import com.bank.cqrs.core.handlers.EventSourcingHandler;
import com.bank.cqrs.core.infrastructure.EventStore;
import com.bank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    private final EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.saveEvents(aggregateRoot.getId(), aggregateRoot.getUncommittedChanges(), aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        AccountAggregate accountAggregate = new AccountAggregate();
        List<BaseEvent> events = eventStore.findByAggregateId(id);
        if (!CollectionUtils.isEmpty(events)) {
            accountAggregate.loadFromHistory(events);
            Optional<Integer> maxVersion = events.stream().map(event -> event.getVersion()).max(Comparator.naturalOrder());
            accountAggregate.setVersion(maxVersion.orElse(0));
        }
        return accountAggregate;
    }

    @Override
    public void rePublishEvents() {
        List<String> aggregateIds = eventStore.findAllAggregateIds();
        if (!CollectionUtils.isEmpty(aggregateIds)) {
            aggregateIds.forEach(aggregateId -> {
                AccountAggregate accountAggregate = getById(aggregateId);
                if (accountAggregate != null && accountAggregate.getIsActive()) {
                    eventStore.findByAggregateId(aggregateId).forEach(event -> {
                        eventProducer.send(event.getClass().getSimpleName(), event);
                    });
                }
            });
        }
    }
}
