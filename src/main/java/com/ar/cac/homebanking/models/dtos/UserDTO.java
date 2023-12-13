package com.ar.cac.homebanking.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String surname;

    private String dni;
    private List<AccountDTO> accounts;

}
