package com.ikarabulut.shareable.account;

import com.ikarabulut.shareable.account.CryptographyService;
import com.ikarabulut.shareable.account.common.SessionModel;
import com.ikarabulut.shareable.account.common.UserModel;

import java.util.Date;

public class SessionService {
    private CryptographyService cryptoService;
    private UserModel user;

    public SessionService(CryptographyService cryptoService, UserModel user) {
        this.cryptoService = cryptoService;
        this.user = user;
    }

    public SessionModel createSession(Date createdDate) {
        var jwt = cryptoService.createLoginJwt(user.getEmail(), new Date());
        var session = new SessionModel();
        session.setUserId(user.getUuid());
        session.setJwt(jwt);
        session.setCreatedAt(createdDate.toString());
        return session;
    }
}
