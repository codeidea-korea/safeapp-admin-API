package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "정책 수정 요청")
@Data
public class RequestPolicyEditDTO {

    @Schema(description = "마지막 수정자(관리자) PK")
    private Long adminId;

    @Schema(description = "내용")
    private String contents;

}