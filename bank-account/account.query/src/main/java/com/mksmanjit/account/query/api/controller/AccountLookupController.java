package com.mksmanjit.account.query.api.controller;

import com.mksmanjit.account.common.dto.BaseResponse;
import com.mksmanjit.account.query.api.dto.AccountLookupResponse;
import com.mksmanjit.account.query.api.dto.EqualityType;
import com.mksmanjit.account.query.api.queries.FindAccountByHolderQuery;
import com.mksmanjit.account.query.api.queries.FindAccountByIdQuery;
import com.mksmanjit.account.query.api.queries.FindAccountWithBalanceQuery;
import com.mksmanjit.account.query.api.queries.FindAllAccountsQuery;
import com.mksmanjit.account.query.domain.BankAccount;
import com.mksmanjit.account.query.infrastructure.AccountQueryDispatcher;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-account-lookup")
@CommonsLog
public class AccountLookupController {

    @Autowired
    private AccountQueryDispatcher dispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accounts  = dispatcher.send(new FindAllAccountsQuery());
            if(accounts == null || accounts.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Bank Accounts fetched successfully").build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get all accounts request";
            log.fatal(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable("id") String accountId) {
        try {
            List<BankAccount> accounts  = dispatcher.send(new FindAccountByIdQuery(accountId));
            if(accounts == null || accounts.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Bank Account fetched successfully").build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Failed to complete get account request for account id: {0}", accountId);
            log.fatal(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byHolder/{accountName}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolderName(@PathVariable("accountName") String accountName) {
        try {
            List<BankAccount> accounts  = dispatcher.send(new FindAccountByHolderQuery(accountName));
            if(accounts == null || accounts.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Bank Account fetched successfully").build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Failed to complete get account request for account name: {0}", accountName);
            log.fatal(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountByBalance(@PathVariable("equalityType") EqualityType equalityType,
                                                                     @PathVariable("balance") double balance) {
        try {
            List<BankAccount> accounts  = dispatcher.send(new FindAccountWithBalanceQuery(equalityType, balance));
            if(accounts == null || accounts.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            AccountLookupResponse response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Bank Accounts fetched successfully").build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get account request for Balance";
            log.fatal(safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
