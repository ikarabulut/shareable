package com.ikarabulut.shareable.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CryptographyService {

    private final Algorithm algo = Algorithm.HMAC256("SuchAGoodSecretLOL");
    private final String ISSUER = "secure-file-server";

    public CryptographyService() {}

    public String createLoginJwt(String username, Date createdDate) {
        var jwtToken = JWT.create()
                .withIssuer(this.ISSUER)
                .withSubject("Session Details")
                .withAudience("admin")
                .withClaim("username", username)
                .withIssuedAt(createdDate)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000L))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(algo);
        return jwtToken;
    }

    public DecodedJWT validateJWT (String token) {
        JWTVerifier verifier = JWT.require(algo)
                .withIssuer(this.ISSUER)
                .build();
        return verifier.verify(token);
    }

}
