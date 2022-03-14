package com.mksmanjit.account.query.infrastructure.consumer;

import com.mksmanjit.account.common.events.AccountCloseEvent;
import com.mksmanjit.account.common.events.AccountOpenedEvent;
import com.mksmanjit.account.common.events.FundDepositEvent;
import com.mksmanjit.account.common.events.FundWithdrawnEvent;
import com.mksmanjit.account.query.infrastructure.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer {

    @Autowired
    private EventHandler eventHandler;

    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent event, Acknowledgment acknowledgment) {
        eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "FundDepositEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundDepositEvent event, Acknowledgment acknowledgment) {
        eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "FundWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundWithdrawnEvent event, Acknowledgment acknowledgment) {
        eventHandler.on(event);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "AccountCloseEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountCloseEvent event, Acknowledgment acknowledgment) {
        eventHandler.on(event);
        acknowledgment.acknowledge();
    }
}
