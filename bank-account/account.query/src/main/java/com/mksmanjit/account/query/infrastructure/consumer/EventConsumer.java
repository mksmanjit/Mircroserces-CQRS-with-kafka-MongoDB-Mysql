package com.mksmanjit.account.query.infrastructure.consumer;

import com.mksmanjit.account.common.events.AccountCloseEvent;
import com.mksmanjit.account.common.events.AccountOpenedEvent;
import com.mksmanjit.account.common.events.FundDepositEvent;
import com.mksmanjit.account.common.events.FundWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment acknowledgment);
    void consume(@Payload FundDepositEvent event, Acknowledgment acknowledgment);
    void consume(@Payload FundWithdrawnEvent event, Acknowledgment acknowledgment);
    void consume(@Payload AccountCloseEvent event, Acknowledgment acknowledgment);
}
