package com.bank.account.cmd.api.controllers;

import com.bank.account.cmd.api.commands.OpenAccountCommand;
import com.bank.account.common.dto.BaseResponse;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/open-account")
@Slf4j
@RequiredArgsConstructor
public class OpenAccountController {

    private final CommandDispatcher commandDispatcher;


    @PostMapping
    public ResponseEntity<BaseResponse> createAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        log.info("Received request to open account: {}", openAccountCommand);
        var uuid = UUID.randomUUID().toString();
        openAccountCommand.setId(uuid);
        try {
            commandDispatcher.send(openAccountCommand);
            return new ResponseEntity<>(new BaseResponse("Account opened successfully"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn("Failed to open account: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Couldn't open Account"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn("Failed to open account: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Couldn't open Account"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
