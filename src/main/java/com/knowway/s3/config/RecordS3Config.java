package com.knowway.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * S3 녹음 설정 파일
 *
 * @author 박유진
 * @since 2024.07.30
 * @version 1.0
 */
@Configuration
public class RecordS3Config {
    @Value("${cloud.aws.credentials.recordAccessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.recordSecretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean(name = "recordAmazonS3")
    public AmazonS3 recordAmazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
