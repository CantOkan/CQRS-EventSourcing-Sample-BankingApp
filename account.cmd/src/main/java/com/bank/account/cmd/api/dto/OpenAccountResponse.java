package com.bank.account.cmd.api.dto;

import com.bank.account.common.dto.BaseResponse;
import lombok.Data;

@Data
public class OpenAccountResponse extends BaseResponse {
    private String accountNumber;

    public OpenAccountResponse(String message, String accountNumber) {
        super(message);
        this.accountNumber = accountNumber;
    }
}
