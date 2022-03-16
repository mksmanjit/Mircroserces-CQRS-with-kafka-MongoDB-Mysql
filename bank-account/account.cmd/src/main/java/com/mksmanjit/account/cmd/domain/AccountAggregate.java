package com.mksmanjit.account.cmd.domain;

import com.mksmanjit.account.cmd.api.commands.OpenAccountCommand;
import com.mksmanjit.account.common.events.AccountCloseEvent;
import com.mksmanjit.account.common.events.AccountOpenedEvent;
import com.mksmanjit.account.common.events.FundDepositEvent;
import com.mksmanjit.account.common.events.FundWithdrawnEvent;
import com.mksmanjit.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                 .id(command.getId())
                 .accountHolder(command.getAccountHolder())
                 .createdDate(new Date())
                 .accountType(command.getAccountType())
                 .openingBalance(command.getOpeningBalance())
                 .build());
    }

    public void apply(AccountOpenedEvent event) {
            this.id = event.getId();
            this.active = true;
            this.balance= event.getOpeningBalance();
    }

    public void depositFunds(double amount) {
        if(!this.active) {
            throw new IllegalStateException("Funds cannot be deposited to close Bank account");
        }
        if(amount<=0) {
            throw new IllegalStateException("Deposite amount must be greater than zero");
        }
        raiseEvent(FundDepositEvent.builder().id(this.id).amount(amount).build());
    }

    public void apply(FundDepositEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFund(double amount) {
        if(!this.active) {
            throw new IllegalStateException("Funds cannot be withdraw to close Bank account");
        }
        if(amount<=0) {
            throw new IllegalStateException("Deposite amount must be greater than zero");
        }
        raiseEvent(FundWithdrawnEvent.builder().id(this.id).amount(amount).build());
    }

    public void apply(FundWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if(!this.active) {
            throw new IllegalStateException("The bank account has already been closed");
        }
        raiseEvent(AccountCloseEvent.builder().id(this.id).build());
    }

    public void apply(AccountCloseEvent event) {
        this.id = event.getId();
        this.active = false;
    }

    public double getBalance() {
        return balance;
    }

    public Boolean isActive() {
        return this.active;
    }
}
