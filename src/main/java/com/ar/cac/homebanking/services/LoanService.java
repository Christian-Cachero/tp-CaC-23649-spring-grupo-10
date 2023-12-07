package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.AccountNotFoundException;
import com.ar.cac.homebanking.exceptions.InsufficientFoundsException;
import com.ar.cac.homebanking.exceptions.LoanNotFoundException;
import com.ar.cac.homebanking.mappers.LoanMapper;
import com.ar.cac.homebanking.models.Account;
import com.ar.cac.homebanking.models.Loan;
import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.models.enums.LoanStatus;
import com.ar.cac.homebanking.repositories.AccountRepository;
import com.ar.cac.homebanking.repositories.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository repository;
    private final AccountRepository accountRepository;

    public LoanService(LoanRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public List<LoanDTO> getLoans() {
        List<Loan> loans = repository.findAll();
        return loans.stream()
                .map(LoanMapper::loanToDto)
                .collect(Collectors.toList());
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = repository.findById(id).orElseThrow(() ->
                new LoanNotFoundException("Cuenta no encontrada con id : " + id));
        return LoanMapper.loanToDto(loan);
    }

    public LoanDTO updateLoan(Long id, LoanDTO loanDto) {
        Loan loan = repository.findById(id).orElseThrow(() ->
                new LoanNotFoundException("Cuenta no encontrada con id : " + id));
        Loan updatedLoan = LoanMapper.dtoToLoan(loanDto);
        updatedLoan.setId(loan.getId());
        return LoanMapper.loanToDto(repository.save(updatedLoan));
    }

    public String deleteLoan(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Se ha eliminado el préstamo";
        } else {
            return "No se ha eliminado el préstamo";
        }
    }

    @Transactional
    public LoanDTO createLoan(LoanDTO loanDto) {
        // Comprobar si la cuenta existe
        Account account = accountRepository.findById(loanDto.getUserId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con id : " + loanDto.getUserId()));

        // Crear el préstamo y asignarlo a la cuenta
        Loan loan = LoanMapper.dtoToLoan(loanDto);
        loan.setUser(account.getOwner());
        loan.setStatus(LoanStatus.ACTIVE); // Puedes establecer el estado inicial según tus necesidades

        // Guardar el préstamo en la base de datos
        loan = repository.save(loan);

        // Actualizar el monto de la cuenta con el monto del préstamo
        account.setAmount(account.getAmount().add(loanDto.getAmount()));
        accountRepository.save(account);

        // Devolver el DTO del préstamo con información detallada
        return LoanMapper.loanToDto(loan);
    }
}