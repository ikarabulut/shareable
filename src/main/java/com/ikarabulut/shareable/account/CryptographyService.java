package com.ikarabulut.shareable.account;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class CryptographyService {

    private Algorithm algo;
    private JWTVerifier verifier;

    public CryptographyService(Algorithm algo, JWTVerifier verifier) {
        this.algo = algo;
        this.verifier = verifier;
    }

//    public String createLoginJwt(String username, String password) {
//        var jwtToken =
//    }
}
