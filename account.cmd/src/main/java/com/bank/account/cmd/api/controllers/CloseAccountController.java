package com.bank.account.cmd.api.controllers;

import com.bank.account.cmd.api.commands.CloseAccountCommand;
import com.bank.account.common.dto.BaseResponse;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/close-account")
@Slf4j
@RequiredArgsConstructor
public class CloseAccountController {

    private final CommandDispatcher commandDispatcher;

    @DeleteMapping("/{accountId}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable("accountId") String accountId) {
        log.info("Received request to close account: {}", accountId);
        try {
            commandDispatcher.send(new CloseAccountCommand(accountId));
            return new ResponseEntity<>(new BaseResponse("Close account request completed"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn("Failed to close account: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Couldn't close account"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn("Failed to close account: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
