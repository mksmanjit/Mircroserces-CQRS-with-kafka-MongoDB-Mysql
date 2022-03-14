package com.mksmanjit.account.cmd.api.commands;

import com.mksmanjit.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class WithdrawFundCommand extends BaseCommand {
    private double amount;
}
