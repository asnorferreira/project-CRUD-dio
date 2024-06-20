package dio.jpa.jpa.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JWTCreator {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    // Método para criar um token JWT
    public static String create(String prefix, String key, JWTObject jwtObject) {
        // Conversão da chave de assinatura para SecretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

        // Criação do token JWT
        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject())
                .setIssuedAt(jwtObject.getIssuedAt())
                .setExpiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return prefix + " " + token;
    }

    // Método para interpretar um token JWT e retornar um JWTObject
    public static JWTObject create(String token, String prefix, String key) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token.replace(prefix, "").trim())
                            .getBody();

        JWTObject jwtObject = new JWTObject();
        jwtObject.setSubject(claims.getSubject());
        jwtObject.setIssuedAt(claims.getIssuedAt());
        jwtObject.setExpiration(claims.getExpiration());
        jwtObject.setRoles(((List<?>) claims.get(ROLES_AUTHORITIES)).stream()
                            .map(String::valueOf)
                            .collect(Collectors.toList()));
        return jwtObject;
    }

    private static List<String> checkRoles(List<String> roles) {
        return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_", ""))).collect(Collectors.toList());
    }
}