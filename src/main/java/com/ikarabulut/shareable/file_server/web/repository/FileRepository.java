package com.ikarabulut.shareable.file_server.web.repository;

import com.ikarabulut.shareable.file_server.common.models.FileModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends CrudRepository<FileModel, Long> {
    Optional<FileModel> findByUuid(UUID uuid);
}
