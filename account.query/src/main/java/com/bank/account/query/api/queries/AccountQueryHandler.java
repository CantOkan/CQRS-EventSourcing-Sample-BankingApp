package com.bank.account.query.api.queries;

import com.bank.account.query.domain.AccountRepository;
import com.bank.account.query.domain.BankAccount;
import com.bank.cqrs.core.domian.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountQueryHandler implements QueryHandler {

    private final AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {

        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> accounts = new ArrayList<>();
        bankAccounts.forEach(bankAccount -> accounts.add(bankAccount));
        return accounts;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        Optional<BankAccount> optionalBankAccount = accountRepository.findById(query.getAccountHolderId());
        if (optionalBankAccount.isEmpty()) {
            return null;
        }
        List<BaseEntity> accounts = new ArrayList<>();
        accounts.add(optionalBankAccount.get());
        return accounts;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        Optional<BankAccount> accountOptional = accountRepository.findById(query.getId());
        if (accountOptional.isEmpty())
            return null;
        List<BaseEntity> accounts = new ArrayList<>();
        accounts.add(accountOptional.get());
        return accounts;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsWithBalanceQuery query) {

        switch (query.getEqualityType()) {
            case GREATER_THAN:
                return accountRepository.findByBalanceGreaterThan(query.getBalance());
            case LESS_THAN:
                return accountRepository.findByBalanceLessThan(query.getBalance());
            case EQUAL:
                return accountRepository.findByBalance(query.getBalance());
            default:
                return null;
        }
    }
}
