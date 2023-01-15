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

    @Schema(description = "이메일")
    String email;

    @Schema(description = "비밀번호")
    String password;

    @Schema(description = "휴대폰번호")
    String phoneNo;

    @Schema(description = "회원 유형")
    UserType type;

    @Schema(description = "회원 아이디")
    String userID;

    @Schema(description = "회원명")
    String userName;

    @Schema(description = "SNS 동의 여부")
    YN snsAllowed;

    @Schema(description = "SNS 유형")
    SNSType snsType = SNSType.NORMAL;

    @Schema(description = "마케팅 유무")
    YN marketingAllowed;

    @Schema(description = "마케팅 유무 시간")
    LocalDateTime marketingAllowedAt;

    @Schema(description = "메세지 동의 여부")
    YN messageAllowed;

    @Schema(description = "메세지 동의 시각")
    LocalDateTime messageAllowedAt;
    
}
