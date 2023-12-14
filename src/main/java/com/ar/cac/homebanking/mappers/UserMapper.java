package com.ar.cac.homebanking.mappers;

import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import lombok.experimental.UtilityClass;

//import java.util.ArrayList;
//import java.util.List;

@UtilityClass
public class UserMapper {

    // Metodos para transformar objetos

    public static User dtoToUser(UserDTO dto){
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setDni(dto.getDni());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

  /*  public static List<User> dtoToUsers(List<UserDTO> dtos){
        List<User> users = new ArrayList<>();
        for (UserDTO dto : dtos) {
            users.add(dtoToUser(dto));
        }
        return users;
    }
*/

    public static UserDTO userToDto(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setDni(user.getDni());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

   /* public static List<UserDTO> usersToDto(List<User> users){
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(userToDto(user));
        }
        return dtos;
    }*/
}
