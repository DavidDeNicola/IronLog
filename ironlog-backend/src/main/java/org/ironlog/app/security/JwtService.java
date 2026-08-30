package org.ironlog.app.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.ironlog.app.model.Utente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Utente utente){
        long oggiMillis = System.currentTimeMillis();
        Date iat = new Date(oggiMillis);
        Date exp = new Date(oggiMillis + expirationMs);

        return Jwts.builder()
                .subject(utente.getEmail())
                .issuer("IronLog")
                .issuedAt(iat)
                .expiration(exp)
                .claims()
                .add("ruolo", utente.getRuolo().name())
                .and()
                .signWith(secretKey())
                .compact();
    }

    public String getSubject(String token){
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey())
                .build();

        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
