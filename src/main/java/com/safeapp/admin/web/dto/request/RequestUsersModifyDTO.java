package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "회원 수정 요청")
@Data
public class RequestUsersModifyDTO {

    @Schema(description = "이름", example = "회원 1")
    String userName;

    @Schema(description = "이메일", example = "user1@codeidea.dev")
    String email;

    @Schema(description = "휴대폰번호", example = "010-1111-1111")
    String phoneNo;

    @Schema(description = "메일 알림 동의 여부", example = "false")
    Boolean emailAllowed;

    @Schema(description = "마케팅 정보 수신 동의 여부", example = "1")
    YN marketingAllowed;

    @Schema(description = "마케팅 정보 수신 동의 시각")
    LocalDateTime marketingAllowedAt;

}