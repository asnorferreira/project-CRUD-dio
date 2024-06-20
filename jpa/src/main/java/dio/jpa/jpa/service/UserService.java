package dio.jpa.jpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dio.jpa.jpa.dtos.UserDto;
import dio.jpa.jpa.model.User;
import dio.jpa.jpa.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public User registerNewUser(UserDto userDto) {
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(encodePassword(userDto.getPassword()));
        newUser.setRoles(userDto.getRoles());

        return repository.save(newUser);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public void deleteUser(Integer id) {
        repository.deleteById(id);
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }
}
