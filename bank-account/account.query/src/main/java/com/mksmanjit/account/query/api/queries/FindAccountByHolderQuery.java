package com.mksmanjit.account.query.api.queries;

import com.mksmanjit.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountByHolderQuery extends BaseQuery {
    private String holderName;
}
