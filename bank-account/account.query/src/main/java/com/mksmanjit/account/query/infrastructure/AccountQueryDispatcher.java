package com.mksmanjit.account.query.infrastructure;

import com.mksmanjit.cqrs.core.domain.BaseEntity;
import com.mksmanjit.cqrs.core.infrastructure.QueryDispatcher;
import com.mksmanjit.cqrs.core.queries.BaseQuery;
import com.mksmanjit.cqrs.core.queries.QueryHandlerMethod;
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
       List<QueryHandlerMethod> handlers = routes.computeIfAbsent(type,c-> new LinkedList<>());
        handlers.add(handler);

    }

    @Override
    public <U extends BaseEntity> List<U> send(BaseQuery query) {
        List<QueryHandlerMethod> handlers = routes.get(query.getClass());
        if(handlers == null || handlers.size() == 0) {
            throw new RuntimeException("No Query Handler was registered");
        }

        if(handlers.size() > 1) {
            throw new RuntimeException("We cannot send a query to more than on handler");
        }
        return handlers.get(0).handle(query);
    }
}
