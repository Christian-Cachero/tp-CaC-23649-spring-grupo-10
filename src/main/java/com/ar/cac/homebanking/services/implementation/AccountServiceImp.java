package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.enums.AccountType;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.UserRepository;
import com.ar.cac.homebanking.services.abstraction.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    public AccountServiceImp(AccountRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }
    public Optional<List<AccountDTO>> getAccounts() {
        List<Account> accounts = repository.findAll();
        return Optional.of( accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList()));
        /*return accounts.stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList());*/
    }

    public Optional<AccountDTO> createAccount(AccountDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotExistsException("Usuario no encontrado"));

        // Crear la cuenta
        Account newAccount = Account.builder()
                .type(AccountType.SAVINGS_BANK)
                .cbu(dto.getCbu())
                .alias(dto.getAlias())
                .amount(dto.getAmount())
                .owner(owner)
                .build();

        Account savedAccount = repository.save(newAccount);

        return Optional.of(AccountMapper.accountToDto(savedAccount));
    }

    public Optional<AccountDTO> getAccountById(Long id) {
        return repository.findById(id).map(AccountMapper::accountToDto);

        /*Account entity = repository.findById(id).get();
        return AccountMapper.accountToDto(entity);*/
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

    public Optional<AccountDTO> updateAccount(Long id, AccountDTO dto) {
        if (repository.existsById(id)) {
            Account accountToModify = repository.findById(id).get();

            // Validar qué datos no vienen en null para setearlos al objeto ya creado

            // Logica del Patch
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

           return Optional.of(AccountMapper.accountToDto(accountModified));

        }
        throw new UserNotExistsException("La cuenta a modificar no existe");
    }
}
