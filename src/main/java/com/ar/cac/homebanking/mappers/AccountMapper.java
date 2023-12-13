package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import lombok.Builder;
import lombok.experimental.UtilityClass;

@UtilityClass
@Builder
public class AccountMapper {

    public AccountDTO accountToDto(Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .alias(account.getAlias())
                .cbu(account.getCbu())
                .type(account.getType())
                .amount(account.getAmount())
                .ownerId(account.getOwner() != null ? account.getOwner().getId() : null)
                .build();
    }

    public Account dtoToAccount(AccountDTO dto){
        return Account.builder()
                .alias(dto.getAlias())
                .type(dto.getType())
                .cbu(dto.getCbu())
                .amount(dto.getAmount())
                .build();
    }
}
