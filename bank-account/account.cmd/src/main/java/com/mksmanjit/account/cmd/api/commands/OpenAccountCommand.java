package com.mksmanjit.account.cmd.api.commands;

import com.mksmanjit.account.common.dto.AccountType;
import com.mksmanjit.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
