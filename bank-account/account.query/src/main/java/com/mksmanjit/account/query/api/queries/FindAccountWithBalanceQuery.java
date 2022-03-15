package com.mksmanjit.account.query.api.queries;

import com.mksmanjit.account.query.api.dto.EqualityType;
import com.mksmanjit.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountWithBalanceQuery extends BaseQuery {
    private EqualityType equalityType;
    private double balance;
}
