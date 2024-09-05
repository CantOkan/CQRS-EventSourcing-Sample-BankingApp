package com.bank.cqrs.core.handlers;

import com.bank.cqrs.core.domian.AggregateRoot;

public interface EventSourcingHandler<T> {

    void save(AggregateRoot aggregateRoot);

    T getById(String id);

    void rePublishEvents();
}
