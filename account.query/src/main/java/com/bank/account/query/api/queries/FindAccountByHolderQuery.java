package com.bank.account.query.api.queries;

import com.bank.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAccountByHolderQuery extends BaseQuery {
    private String accountHolderId;
}
