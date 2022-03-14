package com.mksmanjit.account.cmd.api.commands;

import com.mksmanjit.account.cmd.domain.AccountAggregate;
import com.mksmanjit.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcccountCommandHandler implements CommandHandler {

    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.depositFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        if(command.getAmount() > aggregate.getBalance()) {
            throw new IllegalStateException("Withdrawal declined,Insufficient funds!");
        }
        aggregate.withdrawFund(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }
}
