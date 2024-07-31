package com.sh.app.common;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;

public class ImageUpload {
    public void imageUpload() {
        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        final String accessKey = "ncp_iam_BPASKR1pd5uElFPzv8hI";
        final String secretKey = "ncp_iam_BPKSKRElH5DqBGp5Rx33PxHvyCR6HHkxYK";

// S3 client
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String bucketName = "up-bucket";

// create folder
        //내가 올리고싶은 폴더 이름 Package 하위에 올리고싶으면 -> folerName
        String folderName = "Package/";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0L);
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, new ByteArrayInputStream(new byte[0]), objectMetadata);

        try {
            s3.putObject(putObjectRequest);
            System.out.format("Folder %s has been created.\n", folderName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }

// upload local file
        //objectName에다가 저장하고 싶은 폴더 이름 / 내가 올리고 싶은 사진 이름을 입력해야됌.
        String objectName = "Package/패키지2.png";
        // 내가 올리고 싶은 파일의 절대경로 주소 입력 마지막엔 사진이름까지 입력
        String filePath = "/Users/ohyeongdong/Desktop/패키지2.png";

        try {
            s3.putObject(bucketName, objectName, new File(filePath));
            System.out.format("Object %s has been created.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }
}

