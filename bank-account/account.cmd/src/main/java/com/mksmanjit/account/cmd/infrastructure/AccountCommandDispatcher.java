package com.mksmanjit.account.cmd.infrastructure;

import com.mksmanjit.cqrs.core.commands.BaseCommand;
import com.mksmanjit.cqrs.core.commands.CommandHandlerMethod;
import com.mksmanjit.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, c-> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
        var handlers = routes.get(command.getClass());
        if(handlers == null || handlers.size() == 0) {
            throw new RuntimeException("No Command Handler was registered");
        }
        if(handlers.size() > 1) {
            throw new RuntimeException("We cannot send a command to more than on handler");
        }
        handlers.get(0).handle(command);


    }
}
