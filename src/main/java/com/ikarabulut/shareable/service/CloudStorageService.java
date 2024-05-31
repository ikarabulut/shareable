package com.ikarabulut.shareable.service;

import com.ikarabulut.shareable.common.dependency.DependencyFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

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
            uploadResponse = s3Client.putObject(PutObjectRequest.builder().bucket(BUCKET).key(key).build(), RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (AwsServiceException ex) {
            System.out.println(ex.awsErrorDetails().errorMessage());
        } catch (SdkClientException ex) {
            System.out.println(ex.getMessage());
        }

        return uploadResponse.eTag();
    }
}
