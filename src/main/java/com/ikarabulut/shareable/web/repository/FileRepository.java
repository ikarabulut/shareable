package com.ikarabulut.shareable.web.repository;

import com.ikarabulut.shareable.common.models.FileModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends CrudRepository<FileModel, Long> {
    Optional<FileModel> findByUuid(UUID uuid);
}
