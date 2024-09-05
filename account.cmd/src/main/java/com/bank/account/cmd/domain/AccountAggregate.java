package com.bank.account.cmd.domain;

import com.bank.account.cmd.api.commands.OpenAccountCommand;
import com.bank.account.common.events.AccountClosedEvent;
import com.bank.account.common.events.AccountOpenedEvent;
import com.bank.account.common.events.FundsDepositedEvent;
import com.bank.account.common.events.FundsWithdrawEvent;
import com.bank.cqrs.core.domian.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class AccountAggregate extends AggregateRoot {
    private Boolean isActive;
    private double balance;

    public AccountAggregate(OpenAccountCommand openAccountCommand) {
        AccountOpenedEvent accountOpenedEvent = AccountOpenedEvent.builder()
                .id(openAccountCommand.getId())
                .accountHolder(openAccountCommand.getAccountHolder())
                .accountType(openAccountCommand.getAccountType())
                .createdDate(new Date())
                .openingBalance(openAccountCommand.getOpeningBalance())
                .build();

        raiseEvent(accountOpenedEvent);
    }

    public void apply(AccountOpenedEvent accountOpenedEvent) {
        this.id = accountOpenedEvent.getId();
        this.isActive = true;
        this.balance = accountOpenedEvent.getOpeningBalance();

    }

    public void depositeFunds(double amount) {
        if (!this.isActive) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("The deposite amount should be greater than 0");
        }

        FundsDepositedEvent fundsDepositedEvent = FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(fundsDepositedEvent);
    }

    public void apply(FundsDepositedEvent fundsDepositedEvent) {
        this.id = fundsDepositedEvent.getId();
        this.balance += fundsDepositedEvent.getAmount();
    }

    public double getBalance() {
        return this.balance;
    }


    public void withdrawFunds(double amount) {
        if (!this.isActive) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("The withdraw amount should be greater than 0");
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance, cannot withdraw");
        }

        FundsWithdrawEvent fundsWithdrawEvent = FundsWithdrawEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(fundsWithdrawEvent);
    }

    public void apply(FundsWithdrawEvent fundsWithdrawEvent) {
        this.id = fundsWithdrawEvent.getId();
        this.balance -= fundsWithdrawEvent.getAmount();
    }

    public void closeAccount() {
        if (!this.isActive) {
            throw new IllegalStateException("The Account is not active, The account has already been closed ");
        }

        AccountClosedEvent accountClosedEvent = AccountClosedEvent.builder()
                .id(this.id)
                .build();
        raiseEvent(accountClosedEvent);
    }

    public void apply(AccountClosedEvent accountClosedEvent) {
        this.id = accountClosedEvent.getId();
        this.isActive = false;
    }
}
