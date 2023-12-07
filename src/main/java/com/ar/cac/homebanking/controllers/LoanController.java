package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.models.dtos.LoanDTO;
import com.ar.cac.homebanking.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getLoans(){
        List<LoanDTO> loans = service.getLoans();
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id){
        LoanDTO loan = service.getLoanById(id);
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createLoan(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable Long id, @RequestBody LoanDTO loan){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateLoan(id, loan));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteLoan(id));
    }
}