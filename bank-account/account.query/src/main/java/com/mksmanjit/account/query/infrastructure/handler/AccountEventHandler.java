package com.mksmanjit.account.query.infrastructure.handler;

import com.mksmanjit.account.common.events.AccountCloseEvent;
import com.mksmanjit.account.common.events.AccountOpenedEvent;
import com.mksmanjit.account.common.events.FundDepositEvent;
import com.mksmanjit.account.common.events.FundWithdrawnEvent;
import com.mksmanjit.account.query.domain.AccountRepository;
import com.mksmanjit.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundDepositEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (!bankAccount.isPresent()) {
            return;
        }
        double balance = bankAccount.get().getBalance() + event.getAmount();
        bankAccount.get().setBalance(balance);
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(FundWithdrawnEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (!bankAccount.isPresent()) {
            return;
        }
        double balance = bankAccount.get().getBalance() - event.getAmount();
        bankAccount.get().setBalance(balance);
        accountRepository.save(bankAccount.get());
    }

    @Override
    public void on(AccountCloseEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
