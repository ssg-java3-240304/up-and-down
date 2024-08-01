package com.up.and.down.common;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;

public class ImageDelete {
    public  void delete() {
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
        // 삭제 하고싶은 파일의 이름 입력 할 때 폴더 이름도 같이 입력해줘야돼
        // 패키지1.png이름 삭제 하고싶으면 패키지1.png가 존재하는 폴더명 : Package
        // objectName에 Package/패키지1.png
        String objectName = "Package/패키지1.png";

// delete object
        try {
            s3.deleteObject(bucketName, objectName);
            System.out.format("Object %s has been deleted.\n", objectName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }
}