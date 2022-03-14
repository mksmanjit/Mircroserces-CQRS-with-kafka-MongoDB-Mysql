package com.mksmanjit.account.cmd;

import com.mksmanjit.account.cmd.api.commands.*;
import com.mksmanjit.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerHandler() {
		commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(DepositFundCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(WithdrawFundCommand.class, commandHandler::handle);
		commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);
	}

}
