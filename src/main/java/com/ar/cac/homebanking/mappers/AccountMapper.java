package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    public AccountDTO accountToDto(Account account){
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAlias(account.getAlias());
        dto.setCbu(account.getCbu());
        dto.setType(account.getType());
        dto.setAmount(account.getAmount());
        if (account.getOwner() != null) {
            dto.setOwnerId(account.getOwner().getId());  // Asignar el ID del propietario al campo ownerId
        }

        return dto;
    }

    public Account dtoToAccount(AccountDTO dto){
        Account account = new Account();
        account.setAlias(dto.getAlias());
        account.setType(dto.getType());
        account.setCbu(dto.getCbu());
        account.setAmount(dto.getAmount());
        return account;
    }
}
