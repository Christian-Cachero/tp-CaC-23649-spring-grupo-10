package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.models.enums.AccountType;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }
    public List<AccountDTO> getAccounts() {
        List<Account> accounts = repository.findAll();
        return accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList());
    }

    public AccountDTO createAccount(AccountDTO dto) {
        // Verificar si el usuario con el ID proporcionado existe
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotExistsException("Usuario no encontrado"));

        // Crear la cuenta
        Account newAccount = new Account();
        newAccount.setType(AccountType.SAVINGS_BANK);
        newAccount.setCbu(dto.getCbu());
        newAccount.setAlias(dto.getAlias());
        newAccount.setAmount(dto.getAmount());
        newAccount.setOwner(owner);

        // Guardar la cuenta y asociarla al usuario
        Account savedAccount = repository.save(newAccount);

        return AccountMapper.accountToDto(savedAccount);
    }

    public AccountDTO getAccountById(Long id) {
        Account entity = repository.findById(id).get();
        return AccountMapper.accountToDto(entity);
    }

    public String deleteAccount(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "La cuenta con id: " + id + " ha sido eliminada";
        } else {
            // TODO: REFACTOR create new exception
            throw new UserNotExistsException("La cuenta a eliminar no existe");
        }

    }

    public AccountDTO updateAccount(Long id, AccountDTO dto) {
        if (repository.existsById(id)) {
            Account accountToModify = repository.findById(id).get();

            // Validar qué datos no vienen en null para setearlos al objeto ya creado
            if (dto.getAlias() != null) {
                accountToModify.setAlias(dto.getAlias());
            }
            if (dto.getType() != null) {
                accountToModify.setType(dto.getType());
            }
            if (dto.getCbu() != null) {
                accountToModify.setCbu(dto.getCbu());
            }
            if (dto.getAmount() != null) {
                accountToModify.setAmount(dto.getAmount());
            }

            Account accountModified = repository.save(accountToModify);

            return AccountMapper.accountToDto(accountModified);
        }
        throw new UserNotExistsException("La cuenta a modificar no existe");
    }
}
