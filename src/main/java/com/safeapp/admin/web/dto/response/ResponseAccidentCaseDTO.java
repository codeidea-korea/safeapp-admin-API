package com.safeapp.admin.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema
@Data
public class ResponseAccidentCaseDTO {

    @Schema(description = "사고사례 PK")
    @NotBlank(message = NOT_NULL)
    Long id;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

    @Schema(description = "등록자 이름")
    @NotBlank(message = NOT_NULL)
    String userName;

    @Schema(description = "등록일시")
    @NotBlank(message = NOT_NULL)
    LocalDateTime createdDate;

    @Schema(description = "조회수")
    @NotBlank(message = NOT_NULL)
    Integer views;

    @Schema(description = "첨부 이미지(URL)")
    String image;

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