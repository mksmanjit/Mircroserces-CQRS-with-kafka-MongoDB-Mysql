package com.mksmanjit.cqrs.core.producers;

import com.mksmanjit.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}
