package com.bank.account.cmd.api.controllers;

import com.bank.account.cmd.api.commands.RestoreReadDbCommand;
import com.bank.account.common.dto.BaseResponse;
import com.bank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restore-db")
public class RestoreDbController {

    private final CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> restoreDb() {
        try {
            commandDispatcher.send(new RestoreReadDbCommand());
            return ResponseEntity.ok(new BaseResponse("Db restored successfully"));
        } catch (Exception e) {
            log.error("Error while restoring db", e);
            return new ResponseEntity<>(new BaseResponse("Error while restoring db"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
