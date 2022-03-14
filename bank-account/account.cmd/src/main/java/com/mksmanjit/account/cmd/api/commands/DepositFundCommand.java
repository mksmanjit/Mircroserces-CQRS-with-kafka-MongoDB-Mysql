package com.mksmanjit.account.cmd.api.commands;

import com.mksmanjit.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundCommand extends BaseCommand {
    private double amount;
}
