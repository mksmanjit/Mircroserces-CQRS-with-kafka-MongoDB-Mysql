package com.mksmanjit.cqrs.core.handlers;

import com.mksmanjit.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregateRoot);
    T getById(String id);
    void republishEvents();
}
