package com.ChK.PlusTalk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import java.time.Duration;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private static final String BUCKET_NAME = "plustalk-image-bucket"; // ✅ 버킷 이름을 필드로 정의

    @Autowired
    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner; // ✅ S3Presigner를 의존성 주입 받아 사용
    }

    /**
     * 특정 사용자의 프로필 이미지를 위한 Pre-Signed URL을 생성
     */
    public String generateGetPreSignedUrl(long memberId) {
        String objectKey = String.format("image/profile/%s/%s.JPG", memberId, memberId);

        // 객체 존재 여부 확인
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .build());
        } catch (NoSuchKeyException e) {
            return null; // 객체가 존재하지 않으면 null 반환
        } catch (Exception e) {
            return null; // 기타 예외 발생 시에도 null 반환
        }

        // Pre-Signed URL 생성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // 60분 동안 유효
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedRequest.url().toString();
    }

    /**
     * 특정 사용자의 프로필 이미지를 업로드하기 위한 Pre-Signed URL을 생성
     */
    public String generatePutPreSignedUrlForMemberProfileImage(long memberId) {
        String objectKey = String.format("image/profile/%s/%s.JPG", memberId, memberId);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // 60분 동안 유효
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }

    /**
     * 특정 채팅방의 이미지 메시지를 다운로드할 수 있는 Pre-Signed URL을 생성
     */
    public String generatePresignedUrlForChatImage(String chatRoomId, int messageId) {
        String objectKey = "image/chatroom/" + chatRoomId + "/" + messageId + ".JPG";

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // 60분 동안 유효
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    /**
     *  이미지 업로드를 위한 Pre-Signed URL 생성
     */
    public String generatePresignedUrlForChatImageUpload(String chatRoomId, int messageId) {
        String objectKey = "image/chatroom/" + chatRoomId + "/" + messageId +".JPG";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectKey)
//                .contentType("image/JPG") // 기본 Content-Type 설정
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // 60분 동안 유효
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }
}
