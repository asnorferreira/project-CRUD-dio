package dio.jpa.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dio.jpa.jpa.dtos.UserDto;
import dio.jpa.jpa.dtos.UserRoles;
import dio.jpa.jpa.model.User;
import dio.jpa.jpa.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.registerNewUser(userDto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRoles>> getAllUsers() {
        List<User> users = userService.findAll();
        // Use caching or pagination for large data sets
        List<UserRoles> usersRoles = users.stream()
                .map(user -> new UserRoles(
                        user.getName(),
                        user.getUsername(),
                        user.getRoles()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersRoles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}