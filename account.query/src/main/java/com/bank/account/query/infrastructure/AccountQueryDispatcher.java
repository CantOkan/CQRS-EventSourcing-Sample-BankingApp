package com.bank.account.query.infrastructure;

import com.bank.cqrs.core.domian.BaseEntity;
import com.bank.cqrs.core.infrastructure.QueryDispatcher;
import com.bank.cqrs.core.queries.BaseQuery;
import com.bank.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> handler) {
        List<QueryHandlerMethod> queryHandlerMethods = routes.computeIfAbsent(type, c -> new LinkedList<>());
        queryHandlerMethods.add(handler);
    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        List<QueryHandlerMethod> queryHandlerMethods = routes.get(query.getClass());
        if(queryHandlerMethods == null || queryHandlerMethods.size()<=0) {
            throw new IllegalArgumentException("No handler for " + query.getClass());
        }
        if(queryHandlerMethods.size()>1) {
            throw new IllegalArgumentException("More than one handler for " + query.getClass());
        }
        return queryHandlerMethods.get(0).handle(query);
    }
}
