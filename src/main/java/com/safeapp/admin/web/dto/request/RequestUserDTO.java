package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "회원")
@Data
public class RequestUserDTO {

    @Schema(description = "이메일", example = "user1@codeidea.dev")
    String email;

    @Schema(description = "비밀번호", example = "user1_")
    String password;

    @Schema(description = "휴대폰번호", example = "010-1111-2222")
    String phoneNo;

    @Schema(description = "유형", example = "1")
    UserType type;

    @Schema(description = "아이디", example = "user1")
    String userID;

    @Schema(description = "이름", example = "유저 1")
    String userName;

    @Schema(description = "마케팅 정보 수신 동의 여부", example = "1")
    YN marketingAllowed;

    @Schema(description = "마케팅 정보 수신 동의 시각")
    LocalDateTime marketingAllowedAt;
    
}