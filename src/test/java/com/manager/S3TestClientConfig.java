package com.manager;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
public class S3TestClientConfig {

    private final DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");

    private final LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(S3);

    @Value("${s3.base-bucket}")
    private String BASE_BUCKET;

    @Bean
    @Primary
    public AmazonS3 amazonS3ForTests() {
        localstack.start();
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                localstack.getEndpointOverride(S3).toString(),
                                localstack.getRegion()
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build();
        client.createBucket(BASE_BUCKET);
        return client;
    }
}
