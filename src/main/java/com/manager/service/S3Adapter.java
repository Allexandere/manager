package com.manager.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class S3Adapter {

    private final AmazonS3 client;

    @Value("${s3.base-bucket}")
    private String BASE_BUCKET;

    public PutObjectResult createS3Folder(String path) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        PutObjectRequest putObjectRequest = new PutObjectRequest(BASE_BUCKET,
                path + "/",
                emptyContent,
                metadata);

        return client.putObject(putObjectRequest);
    }
}
