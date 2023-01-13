package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "유저")
public class RequestUserDTO {
    @Schema(description = "타입")
    UserType type;

    @Schema(description = "유저ID")
    String userId;

    @Schema(description = "핸드폰번호")
    String phoneNo;

    @Schema(description = "유저 이름")
    String userName;

    @Schema(description = "이메일주소")
    String email;

    @Schema(description = "SNS동의여부")
    YN snsAllowed;

    @Schema(description = "마케팅유무")
    YN marketingAllowed;

    @Schema(description = "마케팅유무시간")
    LocalDateTime marketingAllowedAt;

    @Schema(description = "메시지동의여부")
    YN messageAllowed;

    @Schema(description = "메시지동의시각")
    LocalDateTime messageAllowedAt;

    @Schema(description = "비밀번호")
    String password;

    @Schema(description = "SNS타입")
    SNSType snsType = SNSType.NORMAL;
}
