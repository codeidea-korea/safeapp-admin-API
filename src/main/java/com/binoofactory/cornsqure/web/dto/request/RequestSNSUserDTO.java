package com.binoofactory.cornsqure.web.dto.request;

import com.binoofactory.cornsqure.web.data.SNSType;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import sun.security.krb5.internal.crypto.Des;

@Getter
@Setter
@Schema(description = "SNS유저")
public class RequestSNSUserDTO {
    @Schema(description = "SNS 고유값")
    String snsValue;

    @Schema(description = "이메일")
    String email;

    @Schema(description = "휴대폰번호")
    String phoneNo;

    @Schema(description = "유저이름")
    String userName;

    @Schema(description = "마케팅 정보 수신 동의")
    YN messageAllowed;

    @Schema(description = "유저 타입")
    UserType userType;

    @Schema(description = "SNS타입")
    SNSType snsType;
}
