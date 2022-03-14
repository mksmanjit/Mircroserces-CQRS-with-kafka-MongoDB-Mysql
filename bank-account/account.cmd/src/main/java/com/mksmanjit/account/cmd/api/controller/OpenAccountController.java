package com.mksmanjit.account.cmd.api.controller;

import com.mksmanjit.account.cmd.api.commands.OpenAccountCommand;
import com.mksmanjit.account.cmd.api.dto.OpenAccountResponse;
import com.mksmanjit.account.common.dto.BaseResponse;
import com.mksmanjit.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/open-bank-account")
@CommonsLog
public class OpenAccountController {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse(id, "Bank Account creation request completed successfully"), HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            log.warn(MessageFormat.format("Client made a bad request: {0}", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to open a new bank account for id - {0}", id);
            log.fatal(safeErrorMessage, ex);
            return new ResponseEntity<>(new OpenAccountResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
