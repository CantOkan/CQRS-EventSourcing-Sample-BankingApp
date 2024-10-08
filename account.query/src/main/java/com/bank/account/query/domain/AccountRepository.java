package com.bank.account.query.domain;

import com.bank.cqrs.core.domian.BaseEntity;
import com.bank.cqrs.core.events.BaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountHolder(String accountHolder);
    List<BaseEntity> findByBalanceGreaterThan(double balance);
    List<BaseEntity> findByBalanceLessThan(double balance);
    List<BaseEntity> findByBalance(double balance);
}
