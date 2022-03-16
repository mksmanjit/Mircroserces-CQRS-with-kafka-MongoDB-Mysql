package com.mksmanjit.account.cmd.infrastructure;

import com.mksmanjit.account.cmd.domain.AccountAggregate;
import com.mksmanjit.account.cmd.domain.EventStoreRepository;
import com.mksmanjit.cqrs.core.events.BaseEvent;
import com.mksmanjit.cqrs.core.events.EventModel;
import com.mksmanjit.cqrs.core.exceptions.AggregateNotFoundException;
import com.mksmanjit.cqrs.core.exceptions.ConcurrencyException;
import com.mksmanjit.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private AccountEventProducer accountEventProducer;

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvent(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(expectedVersion != -1 && expectedVersion != eventStream.get(eventStream.size() - 1).getVersion()) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for(var event: events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getName())
                    .version(version)
                    .eventType(event.getClass().getName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if(!persistedEvent.getId().isEmpty()) {
                accountEventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account Id provided");
        }
        return eventStream.stream().map(event -> event.getEventData()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateIds() {
        var eventStream = eventStoreRepository.findAll();
        if(eventStream == null || eventStream.isEmpty()) {
            throw new IllegalStateException("Could not retrieve event stream from the event store");
        } else {
           return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
        }
    }
}
