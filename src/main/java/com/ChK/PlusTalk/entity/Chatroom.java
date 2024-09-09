package com.ChK.PlusTalk.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
public class Chatroom {

    private String chatRoomId;  // DynamoDB의 파티션 키
    private Set<String> participantId;  // DynamoDB의 정렬 키
    private String chatRoomName;
    private LocalDateTime createdTime;

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
