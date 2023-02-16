package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "사고사례 요청")
@Data
public class RequestAccidentCaseDTO {

    @Schema(description = "제목")
    String title;

    @Schema(description = "등록일시")
    LocalDateTime createdAt;

    @Schema(description = "등록자(관리자) PK")
    Long adminId;

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

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "사고일련번호")
    String accidentUid;

}