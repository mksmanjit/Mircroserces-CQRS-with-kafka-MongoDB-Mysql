package com.mksmanjit.account.cmd.api.controller;

import com.mksmanjit.account.cmd.api.commands.RestoreReadDbCommand;
import com.mksmanjit.account.cmd.api.dto.OpenAccountResponse;
import com.mksmanjit.account.common.dto.BaseResponse;
import com.mksmanjit.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/v1/restore-read-db")
@CommonsLog
public class RestoreReadDbController {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> restoreDb(){
        RestoreReadDbCommand restoreReadDbCommand = new RestoreReadDbCommand();
        try {
            commandDispatcher.send(restoreReadDbCommand);
            return new ResponseEntity<>(new BaseResponse("Read database restore request completed successfully"), HttpStatus.OK);
        } catch (IllegalStateException ex) {
            log.warn(MessageFormat.format("Client made a bad request: {0}", ex.toString()));
            return new ResponseEntity<>(new BaseResponse(ex.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            var safeErrorMessage = "Error while processing request to restore the read db";
            log.fatal(safeErrorMessage, ex);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
