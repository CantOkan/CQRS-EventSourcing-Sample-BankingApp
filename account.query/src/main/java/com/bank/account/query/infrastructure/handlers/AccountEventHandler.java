package com.bank.account.query.infrastructure.handlers;

import com.bank.account.common.events.AccountClosedEvent;
import com.bank.account.common.events.AccountOpenedEvent;
import com.bank.account.common.events.FundsDepositedEvent;
import com.bank.account.common.events.FundsWithdrawEvent;
import com.bank.account.query.domain.AccountRepository;
import com.bank.account.query.domain.BankAccount;
import com.bank.account.query.infrastructure.handlers.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .balance(event.getOpeningBalance())
                .createdDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .status(true)
                .build();

        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        BankAccount bankAccount = accountRepository.findById(event.getId()).orElseThrow( () -> new RuntimeException("Account not found"));
        bankAccount.setBalance(bankAccount.getBalance() + event.getAmount());
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawEvent event) {
        BankAccount bankAccount = accountRepository.findById(event.getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        bankAccount.setBalance(bankAccount.getBalance()-event.getAmount());
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.findById(event.getId()).ifPresent(account -> {
            account.setStatus(false);
            accountRepository.save(account);
        });
    }
}
