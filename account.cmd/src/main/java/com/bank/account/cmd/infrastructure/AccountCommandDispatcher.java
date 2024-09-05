package com.bank.account.cmd.infrastructure;

import com.bank.cqrs.core.commands.BaseCommand;
import com.bank.cqrs.core.commands.CommandHandlerMethod;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> commandType, CommandHandlerMethod<T> handler) {
        if (!routes.containsKey(commandType)) {
            routes.put(commandType, new LinkedList<>());
        }
        var handlers = routes.get(commandType);
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());

        if (handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No handler registered for " + command.getClass());
        } else if (handlers.size() > 1) {
            throw new RuntimeException("Multiple handlers registered for " + command.getClass());
        }

        handlers.get(0).handle(command);
    }
}
