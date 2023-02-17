package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "사고사례 수정 요청")
@Data
public class RequestAccidentCaseEditDTO {

    @Schema(description = "제목")
    String title;

    @Schema(description = "태그")
    String tags;

    @Schema(description = "사고명")
    String name;

    @Schema(description = "사고발생일시")
    LocalDateTime accidentAt;

    @Schema(description = "사고경위")
    String accidentReason;

    @Schema(description = "사고원인")
    String accidentCause;

    @Schema(description = "구체적 사고원인")
    String causeDetail;

    @Schema(description = "재발방지대책")
    String response;

    @Schema(description = "이미지 첨부")
    String image;

    @Schema(description = "사고일련번호")
    String accidentUid;

}