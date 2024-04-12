package com.ikarabulut.shareable.repository;

import com.ikarabulut.shareable.models.FileModel;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileModel, Long> {
}
