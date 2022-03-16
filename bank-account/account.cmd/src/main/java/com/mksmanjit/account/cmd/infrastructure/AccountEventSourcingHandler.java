package com.mksmanjit.account.cmd.infrastructure;

import com.mksmanjit.account.cmd.domain.AccountAggregate;
import com.mksmanjit.cqrs.core.domain.AggregateRoot;
import com.mksmanjit.cqrs.core.handlers.EventSourcingHandler;
import com.mksmanjit.cqrs.core.infrastructure.EventStore;
import com.mksmanjit.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregateRoot) {
        eventStore.saveEvent(aggregateRoot.getId(),aggregateRoot.getUncommittedChanges(),aggregateRoot.getVersion());
        aggregateRoot.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if(events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(event -> event.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for(String id : aggregateIds) {
            var aggregate = getById(id);
            if(aggregate == null || !aggregate.isActive()) continue;
            var events = eventStore.getEvents(id);
            for(var event: events) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
            }
        }
    }
}
