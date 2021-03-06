package com.mksmanjit.account.cmd.api.controller;

import com.mksmanjit.account.cmd.api.commands.DepositFundCommand;
import com.mksmanjit.account.cmd.api.commands.OpenAccountCommand;
import com.mksmanjit.account.cmd.api.dto.OpenAccountResponse;
import com.mksmanjit.account.common.dto.BaseResponse;
import com.mksmanjit.cqrs.core.exceptions.AggregateNotFoundException;
import com.mksmanjit.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.MessageFormat;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/deposit-funds")
@CommonsLog
public class DepositFundsController {
    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable ("id") String id,
                                                    @RequestBody DepositFundCommand command) {

        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Deposit Fund request completed successfully"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException ex) {
            log.warn(MessageFormat.format("Client made a bad request: {0}", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to Deposit a fund for bank account for id - {0}", id);
            log.fatal(safeErrorMessage, ex);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
