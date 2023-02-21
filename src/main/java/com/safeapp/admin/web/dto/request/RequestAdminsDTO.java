package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "관리자 요청")
@Data
public class RequestAdminsDTO {

    @Schema(description = "아이디", example = "admin1")
    String adminId;

    @Schema(description = "이메일", example = "admin1@codeidea.dev")
    String email;

    @Schema(description = "비밀번호", example = "admin1_")
    String password;

    @Schema(description = "이름", example = "관리자 1")
    String adminName;

    @Schema(description = "휴대폰번호", example = "01011111111")
    String phoneNo;

    @Schema(description = "메모", example = "메모 1")
    String memo;

    @Schema(description = "마케팅 정보 수신 동의 여부", example = "1")
    YN marketingAllowed;

    @Schema(description = "마케팅 정보 수신 동의 시각")
    LocalDateTime marketingAllowedAt;

    @Schema(description = "관리자 권한 (모든 관리자는 '일반 관리자' 로 동일)", example = "0")
    AdminType adminType;

}