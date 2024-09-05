package com.bank.account.cmd.api.commands;

import com.bank.account.common.dto.AccountType;
import com.bank.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
