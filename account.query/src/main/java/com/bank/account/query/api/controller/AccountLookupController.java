package com.bank.account.query.api.controller;

import com.bank.account.query.api.dto.EqualityType;
import com.bank.account.query.api.queries.FindAccountByHolderQuery;
import com.bank.account.query.api.queries.FindAccountByIdQuery;
import com.bank.account.query.api.queries.FindAccountsWithBalanceQuery;
import com.bank.account.query.api.queries.FindAllAccountsQuery;
import com.bank.account.query.domain.AccountLookupResponse;
import com.bank.account.query.domain.BankAccount;
import com.bank.cqrs.core.domian.BaseEntity;
import com.bank.cqrs.core.infrastructure.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Supplier;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        return handleQuery(() -> queryDispatcher.send(new FindAllAccountsQuery()), "Successfully retrieved account");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {
        return handleQuery(() -> queryDispatcher.send(new FindAccountByIdQuery(id)), "Successfully retrieved account");
    }

    @GetMapping("/holders/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolderId(@PathVariable String accountHolder) {
        return handleQuery(() -> queryDispatcher.send(new FindAccountByHolderQuery(accountHolder)), "Successfully retrieved account");
    }


    @GetMapping("/balance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolderId(@PathVariable double balance, @PathVariable EqualityType equalityType) {
        return handleQuery(() -> queryDispatcher.send(new FindAccountsWithBalanceQuery(equalityType, balance)), "Successfully retrieved account");
    }


    private ResponseEntity<AccountLookupResponse> handleQuery(Supplier<List<BankAccount>> querySupplier, String successMessage) {
        try {
            List<BankAccount> accounts = querySupplier.get();
            if (accounts == null || accounts.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            AccountLookupResponse response = new AccountLookupResponse(successMessage);
            response.setAccountList(accounts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
