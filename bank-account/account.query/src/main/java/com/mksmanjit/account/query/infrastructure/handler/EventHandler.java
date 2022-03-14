package com.mksmanjit.account.query.infrastructure.handler;

import com.mksmanjit.account.common.events.AccountCloseEvent;
import com.mksmanjit.account.common.events.AccountOpenedEvent;
import com.mksmanjit.account.common.events.FundDepositEvent;
import com.mksmanjit.account.common.events.FundWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundDepositEvent event);
    void on(FundWithdrawnEvent event);
    void on(AccountCloseEvent event);
}
