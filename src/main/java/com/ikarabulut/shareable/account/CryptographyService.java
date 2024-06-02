package com.ikarabulut.shareable.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CryptographyService {

    private Algorithm algo = Algorithm.HMAC256("SuchAGoodSecretLOL");

    public CryptographyService() {}

    public String createLoginJwt(String username, Date createdDate) {
        var jwtToken = JWT.create()
                .withIssuer("secure-file-server")
                .withSubject("Session Details")
                .withAudience("admin")
                .withClaim("userId", username)
                .withIssuedAt(createdDate)
                .withExpiresAt(new Date(System.currentTimeMillis() + 5000L))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(algo);
        return jwtToken;
    }
}
