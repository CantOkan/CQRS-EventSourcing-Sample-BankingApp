package com.bank.account.cmd.api.controllers;

import com.bank.account.cmd.api.commands.DepositFundsCommand;
import com.bank.account.common.dto.BaseResponse;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposit-funds")
@RequiredArgsConstructor
@Slf4j
public class DepositFundsController {


    private final CommandDispatcher commandDispatcher;


    @PutMapping("/{accountId}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable String accountId, @RequestBody DepositFundsCommand depositFundsCommand) {
        log.info("Received request to deposit funds: {}", depositFundsCommand);
        try {
            depositFundsCommand.setId(accountId);
            commandDispatcher.send(depositFundsCommand);
            return new ResponseEntity<>(new BaseResponse("Deposit funds request completed"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn("Failed to deposit funds: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Couldn't deposit funds"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn("Failed to deposit funds: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
