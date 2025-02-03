package com.project.conversor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class S3Service {

    //Esse bucket-name está no application.properties
    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public String uploadFile(MultipartFile file) throws IOException {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) //A chave e a chave secreta tão no enviroment
                .build();

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path tempFile = Files.createTempFile("upload-", fileName);
        file.transferTo(tempFile);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        return "https://" + bucketName + ".s3." + System.getenv("AWS_REGION") + ".amazonaws.com/" + fileName;
    }
}