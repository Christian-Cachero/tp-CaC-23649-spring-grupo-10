package com.ar.cac.homebanking.services.implementation;

import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.mappers.AccountMapper;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.AccountDTO;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.repositories.UserRepository;
import com.ar.cac.homebanking.services.abstraction.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    // Inyectar una instancia del Repositorio

    private final UserRepository repository;

    public UserServiceImp(UserRepository userRepository) {
        this.repository = userRepository;
    }

    // Metodos

    public Optional<List<UserDTO>> getUsers(){
        // Obtengo la lista de la entidad usuario de la db
        List<User> users = repository.findAll();
        // Mapear cada usuario de la lista hacia un dto
        List<UserDTO> userDtos = users.stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());

        return Optional.of(userDtos);
    }

    /* pruebas sin exit aún.
    public Optional<List<UserDTO>> getUsers(List<Long> ids) {
        List<User> users = repository.findAllById(ids);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = UserMapper.userToDto(user);
            userDTOs.add(userDTO);
        }
        return Optional.of(userDTOs);
    }*/

    public Optional<List<UserDTO>> getUsersByIds(List<Long> ids) {
        List<User> users = repository.findAllById(ids);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = UserMapper.userToDto(user);
            // Obtener y mapear las cuentas del usuario
            List<AccountDTO> accountDTOs = user.getAccounts().stream()
                    .map(AccountMapper::accountToDto)
                    .collect(Collectors.toList());
            userDTO.setAccounts(accountDTOs);
            userDTOs.add(userDTO);
        }
        return Optional.of(userDTOs);
    }
    public Optional<UserDTO> getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotExistsException("Usuario no encontrado"));

        UserDTO userDTO = UserMapper.userToDto(user);

        // Obtener y mapear las cuentas del usuario
        List<AccountDTO> accountDTOs = user.getAccounts().stream()
                .map(AccountMapper::accountToDto)
                .collect(Collectors.toList());

        userDTO.setAccounts(accountDTOs);

        return Optional.of(userDTO);
    }

    public Optional<UserDTO> createUser(UserDTO userDto){
        if(validateUserByEmail(userDto) == null){
            return Optional.of(UserMapper.userToDto(repository
                    .save(UserMapper.dtoToUser(userDto))));
        }
        else {
            throw new UserNotExistsException("Usuario con mail: " + userDto.getEmail() + " ya existe");
        }

        /*User userValidated = validateUserByEmail(userDto);
        if (userValidated == null){
            User userSaved = repository.save(UserMapper.dtoToUser(userDto));
            return UserMapper.userToDto(userSaved);
        } else{
            throw new UserNotExistsException("Usuario con mail: " + userDto.getEmail() + " ya existe");
        }*/

    }


    public String deleteUser(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return "El usuario con id: " + id + " ha sido eliminado";
        } else {
            throw new UserNotExistsException("El usuario a eliminar elegido no existe");
        }

    }

    public Optional<UserDTO> updateUser(Long id, UserDTO dto) {
        if (repository.existsById(id)){
            User userToModify = repository.findById(id).get();
            // Validar qué datos no vienen en null para setearlos al objeto ya creado

            // Logica del Patch
            if (dto.getName() != null){
                userToModify.setName(dto.getName());
            }

            if (dto.getSurname() != null){
                userToModify.setSurname(dto.getSurname());
            }

            if (dto.getEmail() != null){
                userToModify.setEmail(dto.getEmail());
            }

            if (dto.getPassword() != null){
                userToModify.setPassword(dto.getPassword());
            }

            if (dto.getDni() != null){
                userToModify.setDni(dto.getDni());
            }

            User userModified = repository.save(userToModify);

            return Optional.of(UserMapper.userToDto(userModified));
        }
        throw new UserNotExistsException("La cuenta a modificar no existe");
    }

    // Validar que existan usuarios unicos por mail
    public User validateUserByEmail(UserDTO dto){
        return repository.findByEmail(dto.getEmail());
    }
}
