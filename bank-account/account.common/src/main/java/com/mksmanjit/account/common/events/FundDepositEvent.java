package com.mksmanjit.account.common.events;

import com.mksmanjit.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundDepositEvent extends BaseEvent {
    private double amount;
}
