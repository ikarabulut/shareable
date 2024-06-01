package com.ikarabulut.shareable.account.controller;

import com.ikarabulut.shareable.account.UserRepository;
import com.ikarabulut.shareable.account.common.LoginModel;
import com.ikarabulut.shareable.account.common.SessionModel;
import com.ikarabulut.shareable.account.common.UserModel;
import com.ikarabulut.shareable.account.common.exceptions.WrongPasswordException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/user")
    public ResponseEntity<UserModel> createUser(@RequestBody @Valid UserModel userModel) {
        UserModel createdUser = this.userRepository.save(userModel);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping(path="/user/all")
    public ResponseEntity<Iterable<UserModel>> getAllUsers() {
        Iterable<UserModel> users = this.userRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path="/user/{uuid}")
    public ResponseEntity<UserModel> getUser(@PathVariable("uuid") @Valid UUID uuid) {
        UserModel user = this.userRepository.findByUuid(uuid).orElseThrow();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path="/user/login")
    public ResponseEntity<SessionModel> login(@RequestBody @Valid LoginModel loginInfo) {
        UserModel user = this.userRepository.findByEmail(loginInfo.getEmail()).orElseThrow();

        if(!BCrypt.checkpw(loginInfo.getPassword(), user.getPassword())){
            throw new WrongPasswordException("The Password you entered is incorrect");
        }

        var session = new SessionModel();
        return new ResponseEntity<SessionModel>(session, HttpStatus.OK);
    }
}
