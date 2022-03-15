package com.mksmanjit.account.query.api.queries;

import com.mksmanjit.account.query.api.dto.EqualityType;
import com.mksmanjit.account.query.domain.AccountRepository;
import com.mksmanjit.account.query.domain.BankAccount;
import com.mksmanjit.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountQueryHandler implements QueryHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> entities = new ArrayList<>();
        bankAccounts.forEach(entities::add);
        return entities;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        BankAccount account = accountRepository.findByAccountHolder(query.getHolderName()).orElse(null);
        if(account == null) return null;
        List<BaseEntity> entities = new ArrayList<>();
        entities.add(account);
        return entities;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        BankAccount account = accountRepository.findById(query.getId()).orElse(null);
        if(account == null) return null;
        List<BaseEntity> entities = new ArrayList<>();
        entities.add(account);
        return entities;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        if(query.getEqualityType() == EqualityType.GREATER_THAN) {
            return accountRepository.findByBalanceGreaterThan(query.getBalance());
        } else {
            return accountRepository.findByBalanceLessThan(query.getBalance());
        }
    }
}
