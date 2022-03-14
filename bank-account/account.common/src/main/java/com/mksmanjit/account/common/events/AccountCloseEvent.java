package com.mksmanjit.account.common.events;

import com.mksmanjit.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class AccountCloseEvent extends BaseEvent {

}
