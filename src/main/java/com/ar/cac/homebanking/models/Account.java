package com.ar.cac.homebanking.models;


import com.ar.cac.homebanking.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

   /* public Account(){

    }*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_type")
    private AccountType type;

    private String cbu;

    private String alias;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "originAccount", cascade = CascadeType.ALL)
    private List<Transfer> originTransfers;

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL)
    private List<Transfer> targetTransfers;

}
