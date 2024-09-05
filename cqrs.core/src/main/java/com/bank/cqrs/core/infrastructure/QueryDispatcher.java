package com.bank.cqrs.core.infrastructure;

import com.bank.cqrs.core.domian.BaseEntity;
import com.bank.cqrs.core.queries.BaseQuery;
import com.bank.cqrs.core.queries.QueryHandlerMethod;

import java.util.List;

public interface QueryDispatcher {
    <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler);

    <U extends BaseEntity> List<U> send(BaseQuery query);
}
