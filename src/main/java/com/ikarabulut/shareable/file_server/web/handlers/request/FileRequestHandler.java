package com.ikarabulut.shareable.file_server.web.handlers.request;

import com.ikarabulut.shareable.account.CryptographyService;
import com.ikarabulut.shareable.file_server.common.AllowedFileTypes;
import com.ikarabulut.shareable.file_server.common.exceptions.FileExtensionNotAllowed;
import com.ikarabulut.shareable.file_server.common.exceptions.InvalidUploadException;
import com.ikarabulut.shareable.file_server.common.exceptions.ResourceNotFoundException;
import com.ikarabulut.shareable.file_server.common.exceptions.UnauthorizedException;
import com.ikarabulut.shareable.file_server.common.models.FileModel;
import com.ikarabulut.shareable.file_server.service.CloudStorageService;
import com.ikarabulut.shareable.file_server.web.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileRequestHandler {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CryptographyService cryptoService;
    private final CloudStorageService cloudStorageService;
    private final HttpServletRequest request;

    public FileRequestHandler(HttpServletRequest request, CloudStorageService cloudStorageService) {
        this.request = request;
        this.cloudStorageService = cloudStorageService;
    }

    public FileModel createFileObject(FileModel fileModel) {
        var decodedJwt = this.cryptoService.validateJWT(request.getHeader("X-token"));

        var extensionIndex = fileModel.getName().lastIndexOf('.');
        var extension = fileModel.getName().substring(extensionIndex);
        if (!AllowedFileTypes.isFileTypeAllowed(extension)) {
            throw new FileExtensionNotAllowed(extension + " is not an allowed extension");
        }

        fileModel.setExtension(extension);
        fileModel.setOwnedBy(decodedJwt.getClaim("username").asString());
        FileModel savedFile = this.fileRepository.save(fileModel);

        return this.fileRepository.findByUuid(savedFile.getUuid()).orElseThrow();
    }

    public FileModel uploadFileToS3(MultipartFile file, UUID uuid) throws IOException {
        var decodedJwt = this.cryptoService.validateJWT(request.getHeader("X-token"));

        Optional<FileModel> fileObj = this.fileRepository.findByUuid(uuid);

        if (fileObj.isEmpty()) {
            throw new ResourceNotFoundException("File is empty");
        }

        FileModel fileRecord = fileObj.get();
        if (!Objects.equals(fileRecord.getOwnedBy(), decodedJwt.getClaim("username").asString())) throw new UnauthorizedException("You are not authorized to act on this file");

        Tika tika = new Tika();
        if (!AllowedFileTypes.getByExtension(fileRecord.getExtension()).getMimeType().equals(tika.detect(file.getBytes()))) {
            throw new FileExtensionNotAllowed("Invalid content type. Expected " + AllowedFileTypes.getByExtension(fileRecord.getExtension()) + " but got " + tika.detect(file.getBytes()));
        }

        if (!Objects.equals(file.getOriginalFilename(), fileRecord.getName())) {
            throw new InvalidUploadException("File name being uploaded does not match file object record");
        }

        var uploadedChecksum = cloudStorageService.uploadFileToBucket(fileRecord.getName(), file);

        fileRecord.setSignature(uploadedChecksum);
        fileRepository.save(fileRecord);

        return fileRecord;
    }

    public Iterable<FileModel> getAllFileObjects(){
        var decodedJwt = this.cryptoService.validateJWT(request.getHeader("X-token"));
        return this.fileRepository.findAll();
    }

    public FileModel getFileObject(Long id) {
        var decodedJwt = this.cryptoService.validateJWT(request.getHeader("X-token"));
        return this.fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No file found with Id: " + id));
    }

    public void deleteFile(Long id) {
        var decodedJwt = this.cryptoService.validateJWT(request.getHeader("X-token"));

        Optional<FileModel> fileObj = this.fileRepository.findById(id);
        if (fileObj.isEmpty()) {
            throw new ResourceNotFoundException("File not found");
        }

        FileModel fileRecord = fileObj.get();
        if (!Objects.equals(fileRecord.getOwnedBy(), decodedJwt.getClaim("username").asString())) throw new UnauthorizedException("You are not authorized to act on this file");

        var isDeleted = cloudStorageService.deleteFileFromBucket(fileRecord.getName());

        if (!isDeleted) {
            throw new RuntimeException("S3 Error");
        }

        fileRepository.delete(fileRecord);
    }
}
