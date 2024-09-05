package com.bank.account.cmd.api.commands;

import com.bank.account.cmd.domain.AccountAggregate;
import com.bank.cqrs.core.handlers.EventSourcingHandler;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand openAccountCommand) {
        AccountAggregate accountAggregate = new AccountAggregate(openAccountCommand);
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(DepositFundsCommand depositFundsCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(depositFundsCommand.getId());
        accountAggregate.depositeFunds(depositFundsCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand withdrawFundsCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(withdrawFundsCommand.getId());
        if (accountAggregate.getBalance() < withdrawFundsCommand.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        accountAggregate.withdrawFunds(withdrawFundsCommand.getAmount());
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(CloseAccountCommand closeAccountCommand) {
        AccountAggregate accountAggregate = eventSourcingHandler.getById(closeAccountCommand.getId());
        accountAggregate.closeAccount();
        eventSourcingHandler.save(accountAggregate);
    }

    @Override
    public void handle(RestoreReadDbCommand restoreReadDbCommand) {
        eventSourcingHandler.rePublishEvents();
    }
}
