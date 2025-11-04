package ru.netology.diplom.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String generateToken(String login) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("login", login)
                .withIssuedAt(new Date())
                .withIssuer("JOB TRACKER APPLICATION")
                .sign(Algorithm.HMAC256(jwtSigningKey));
    }

    public String verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSigningKey))
                .withSubject("User Details")
                .withIssuer("JOB TRACKER APPLICATION")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("login").asString();
    }

}
