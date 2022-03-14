package com.mksmanjit.cqrs.core.infrastructure;


import com.mksmanjit.cqrs.core.commands.BaseCommand;
import com.mksmanjit.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
