package com.ikarabulut.shareable.account;

import com.ikarabulut.shareable.account.common.UserModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserModel, Long> {
    Optional<UserModel> findByUuid(UUID uuid);
    Optional<UserModel> findByUsername(String username);
}
