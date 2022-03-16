package com.mksmanjit.cqrs.core.infrastructure;

import com.mksmanjit.cqrs.core.events.BaseEvent;

import java.util.List;

public interface EventStore {
    void saveEvent(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);
    List<BaseEvent> getEvents(String aggregateId);
    List<String> getAggregateIds();
}
