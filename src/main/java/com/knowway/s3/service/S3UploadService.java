package com.knowway.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * S3 업로드 서비스
 *
 * @author 김진규, 박유진
 * @since 2024.07.25
 * @version 1.0
 */
@Service
public class S3UploadService {

    private final AmazonS3 imageAmazonS3;
    private final AmazonS3 recordAmazonS3;

    @Value("${cloud.aws.s3.imageBucket}")
    private String imageBucket;

    @Value("${cloud.aws.s3.recordBucket}")
    private String recordBucket;

    public S3UploadService(@Qualifier("imageAmazonS3") AmazonS3 imageAmazonS3,
                           @Qualifier("recordAmazonS3") AmazonS3 recordAmazonS3) {
        this.imageAmazonS3 = imageAmazonS3;
        this.recordAmazonS3 = recordAmazonS3;
    }

    public String saveFile(MultipartFile multipartFile, String fileType) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String bucket = getBucketName(fileType);
        AmazonS3 amazonS3 = getAmazonS3Client(fileType);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public UrlResource downloadFile(String originalFilename, String fileType) {
        String bucket = getBucketName(fileType);
        AmazonS3 amazonS3 = getAmazonS3Client(fileType);
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));
        return urlResource;
    }

    public void deleteFile(String originalFilename, String fileType) {
        String bucket = getBucketName(fileType);
        AmazonS3 amazonS3 = getAmazonS3Client(fileType);
        amazonS3.deleteObject(bucket, originalFilename);
    }

    private String getBucketName(String fileType) {
        if ("record".equalsIgnoreCase(fileType)) {
            return recordBucket;
        }
        return imageBucket;
    }

    private AmazonS3 getAmazonS3Client(String fileType) {
        if ("record".equalsIgnoreCase(fileType)) {
            return recordAmazonS3;
        }
        return imageAmazonS3;
    }
}

