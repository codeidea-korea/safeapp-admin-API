package com.binoofactory.cornsqure.web.dto.request;

import com.binoofactory.cornsqure.web.data.SNSType;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import sun.security.krb5.internal.crypto.Des;

import java.time.LocalDateTime;

import static com.binoofactory.cornsqure.web.data.SNSType.NORMAL;

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
    SNSType snsType = NORMAL;
}
