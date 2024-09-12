package com.ChK.PlusTalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 JSON에서 제외
public class FriendRequestDto {
    private String memberEmail;
    private String friendMemberEmail;
}
