package com.ChK.PlusTalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class S3Service {
    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String generateGetPreSignedUrl(String email) {
        // 객체의 키 경로 생성
        String bucketName = "plustalk-image-bucket";
        String objectKey = String.format("image/profile/%s/%s.JPG", email, email);

        // 객체 존재 여부 확인
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.headObject(headObjectRequest);
//            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

        } catch (NoSuchKeyException e) {
            // 객체가 존재하지 않으면 "null" 반환
            return null;
        } catch (Exception e) {
            // 기타 예외 발생 시에도 "null" 반환
            return null;
        }

        // 객체가 존재하면 S3Presigner를 사용하여 프리사인드 URL 생성
        S3Presigner presigner = S3Presigner.create();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL의 유효 기간 설정
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(getObjectPresignRequest);

        // 프리사인드 URL 반환
        return presignedRequest.url().toString();
    }

    // S3에 업로드할 수 있는 pre-signed URL 생성 메서드
    public String generatePutPreSignedUrl(String email) {
        // S3 버킷과 객체 키 설정
        String bucketName = "plustalk-image-bucket";
        String objectKey = String.format("image/profile/%s/%s.JPG", email, email);

        // S3Presigner 생성
        S3Presigner presigner = S3Presigner.create();

        // PutObjectRequest 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
//                .contentType("audio/mpeg")  // Content-Type 설정 - 필요시 추가
                .build();

        // Pre-signed URL 생성 요청 설정
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL의 유효 기간 설정
                .putObjectRequest(putObjectRequest)
                .build();

        // Pre-signed URL 생성
        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        // Pre-signed URL 반환
        return presignedRequest.url().toString();
    }

}
