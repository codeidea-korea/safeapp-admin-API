package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Schema(description = "사고사례")
@Data
public class RequestAccidentCaseDTO {

    @Schema(description = "제목")
    String title;

    @Schema(description = "등록자 PK")
    Long adminId;

    @Schema(description = "태그")
    String tags;

    @Schema(description = "이름")
    String name;

    @Schema(description = "발생일시")
    LocalDateTime accidentAt;

    @Schema(description = "일련번호")
    String accidentUid;

    @Schema(description = "발생원인 분류")
    String accidentCause;

    @Schema(description = "발생원인")
    String accidentReason;

    @Schema(description = "발생원인 상세")
    String causeDetail;

    @Schema(description = "후속조치, 방지책 마련 등")
    String response;

}