package com.mksmanjit.account.query.domain;

import com.mksmanjit.account.common.dto.AccountType;
import com.mksmanjit.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount extends BaseEntity {
    @Id
    private String id;
    private String accountHolder;
    private Date creationDate;
    private AccountType accountType;
    private double balance;


}
