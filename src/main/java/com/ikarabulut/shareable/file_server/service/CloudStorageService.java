package com.ikarabulut.shareable.file_server.service;

import com.ikarabulut.shareable.file_server.common.dependency.DependencyFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class CloudStorageService {
    private final S3Client s3Client;
    private final String BUCKET = "secure-file-api-storage";

    public CloudStorageService() {
        s3Client = DependencyFactory.s3Client();
    }

    public String uploadFileToBucket(String key, MultipartFile file) throws IOException {
        PutObjectResponse uploadResponse = null;

        try {
            var inputStream = file.getInputStream();
            uploadResponse = s3Client.putObject(PutObjectRequest.builder().bucket(BUCKET).key(key).checksumAlgorithm(ChecksumAlgorithm.SHA256).build(), RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (AwsServiceException ex) {
            System.out.println(ex.awsErrorDetails().errorMessage());
        } catch (SdkClientException ex) {
            System.out.println(ex.getMessage());
        }

        assert uploadResponse != null;
        return uploadResponse.checksumSHA256();
    }

    public boolean deleteFileFromBucket(String key) {
        var s3Response = s3Client.deleteObject(DeleteObjectRequest.builder().bucket(BUCKET).key(key).build());
        return s3Response.sdkHttpResponse().isSuccessful();
    }
}
