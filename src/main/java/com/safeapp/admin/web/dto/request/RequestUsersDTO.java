package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "회원 요청")
@Data
public class RequestUsersDTO {

    @Schema(description = "이메일", example = "user1@codeidea.dev")
    String email;

    @Schema(description = "비밀번호", example = "user1_")
    String password;

    @Schema(description = "휴대폰번호", example = "01011111111")
    String phoneNo;

    @Schema(description = "유형", example = "1")
    UserType userType;

    @Schema(description = "아이디", example = "user1")
    String userId;

    @Schema(description = "이름", example = "회원 1")
    String userName;

    @Schema(description = "마케팅 정보 수신 동의 여부", example = "0")
    YN marketingAllowed;

    @Schema(description = "마케팅 정보 수신 동의 시각")
    LocalDateTime marketingAllowedAt;
    
}