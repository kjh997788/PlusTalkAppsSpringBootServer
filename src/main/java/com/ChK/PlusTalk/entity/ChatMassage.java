package com.ChK.PlusTalk.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ChatMassage {
    private String chatRoomId;    // DynamoDB의 파티션 키
    private LocalDateTime messageTime;  // DynamoDB의 정렬 키
    private String messageId;
    private String memberId;
    private String messageText;
    private String imageUrl;
    private String imageThumbnailUrl;

    public static class LocalDateTimeConverter {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        public static String convert(LocalDateTime time) {
            return time.format(FORMATTER);
        }

        public static LocalDateTime unconvert(String stringValue) {
            return LocalDateTime.parse(stringValue, FORMATTER);
        }
    }
}
