package com.manager.unit;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.manager.S3TestClientConfig;
import com.manager.service.S3Adapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(S3TestClientConfig.class)
public class S3AdapterTest {

    @Autowired
    private S3Adapter s3Adapter;

    @Autowired
    private AmazonS3 client;

    @Value("${s3.base-bucket}")
    private String BASE_BUCKET;

    @Test
    void happyCreateFolderTest() {
        String folder = "test-folder";
        s3Adapter.createS3Folder(folder);

        assertTrue(doesFolderExists(folder));
    }

    private boolean doesFolderExists(String folder) {
        return client.listObjects(BASE_BUCKET)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .anyMatch(key -> key.endsWith(folder + "/"));
    }
}
