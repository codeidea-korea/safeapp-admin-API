package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "멤버쉽 결제 수정 요청")
@Data
public class RequestMembershipEditDTO {

    @Schema(description = "멤버쉽 기간 시작일시", example = "2023-02-15T00:00:00.000000")
    String efectiveStartAt;

    @Schema(description = "멤버쉽 기간 종료일시", example = "2023-03-16T00:00:00.000000")
    String efectiveEndAt;

    @Schema(description = "메모", example = "메모 1")
    String memo;
    
}