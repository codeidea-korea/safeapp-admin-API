package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "관리자 수정 요청")
@Data
public class RequestAdminsEditDTO {

    @Schema(description = "아이디", example = "admin1")
    String adminId;

    @Schema(description = "이름", example = "관리자 1")
    String adminName;

    @Schema(description = "휴대폰번호", example = "010-1111-1111")
    String phoneNo;

    @Schema(description = "메모", example = "메모 1")
    String memo;

}