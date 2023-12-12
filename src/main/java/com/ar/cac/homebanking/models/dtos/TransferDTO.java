package com.ar.cac.homebanking.models.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransferDTO {

    private Long id;

    private AccountDTO originAccount; // esto

    private AccountDTO targetAccount; // esto

    private Date date;

    private BigDecimal amount; // y esto
}
