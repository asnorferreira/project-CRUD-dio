package dio.jpa.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import dio.jpa.jpa.dtos.Login;
import dio.jpa.jpa.dtos.Session;
import dio.jpa.jpa.model.User;
import dio.jpa.jpa.repository.UserRepository;
import dio.jpa.jpa.security.JWTCreator;
import dio.jpa.jpa.security.JWTObject;
import dio.jpa.jpa.security.SecurityConfig;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private SecurityConfig securityConfig;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        try {
            // Buscar o usuário pelo username
            User user = repository.findByUsername(login.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Verificar se a senha está correta
            boolean passwordOk = encoder.matches(login.getPassword(), user.getPassword());
            if (!passwordOk) {
                throw new RuntimeException("Senha inválida");
            }

            // Criar a sessão JWT
            JWTObject jwtObject = new JWTObject();
            jwtObject.setSubject(user.getUsername());
            jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
            jwtObject.setExpiration(new Date(System.currentTimeMillis() + securityConfig.getExpiration()));
            jwtObject.setRoles(user.getRoles());

            Session session = new Session();
            session.setLogin(user.getUsername());

            try {
                String token = JWTCreator.create(securityConfig.getPrefix(), securityConfig.getKey(), jwtObject);
                session.setToken(token);

            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar token JWT", e);
            }

            // Retornar a sessão como resposta
            return ResponseEntity.ok(session);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro na autenticação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor. Por favor, tente novamente mais tarde.");
        }
    }
}
