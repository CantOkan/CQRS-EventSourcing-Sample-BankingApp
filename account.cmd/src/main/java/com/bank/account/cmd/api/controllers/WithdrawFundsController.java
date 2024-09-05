package com.bank.account.cmd.api.controllers;


import com.bank.account.cmd.api.commands.WithdrawFundsCommand;
import com.bank.account.common.dto.BaseResponse;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/withdraw-funds")
@Slf4j
@RequiredArgsConstructor
public class WithdrawFundsController {

    private final CommandDispatcher commandDispatcher;

    @PutMapping("/{accountId}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable("accountId") String accountId, @RequestBody WithdrawFundsCommand withdrawFundsCommand) {
        log.info("Received request to withdraw funds: {}", withdrawFundsCommand);
        try {
            withdrawFundsCommand.setId(accountId);
            commandDispatcher.send(withdrawFundsCommand);
            return new ResponseEntity<>(new BaseResponse("Withdraw funds request completed"), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            log.warn("Failed to withdraw funds: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Couldn't deposit funds"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn("Failed to withdraw funds: {}", e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
