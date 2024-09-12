package com.ChK.PlusTalk.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class FriendResponseDto {
    private String memberEmail;
    private String friendMemberEmail;
    private LocalDateTime friendSetTime;
}
