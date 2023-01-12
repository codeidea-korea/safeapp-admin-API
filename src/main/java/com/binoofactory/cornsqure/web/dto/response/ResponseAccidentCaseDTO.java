package com.binoofactory.cornsqure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema
public class ResponseAccidentCaseDTO {
    @Schema(description = "사고사례 ID")
    @NotBlank(message = NOT_NULL)
    Long id;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

    @Schema(description = "유저 이름")
    @NotBlank(message = NOT_NULL)
    String userName;

    @Schema(description = "등록일")
    @NotBlank(message = NOT_NULL)
    LocalDateTime createdDate;

    @Schema(description = "열람횟수")
    @NotBlank(message = NOT_NULL)
    Integer views;

    @Schema(description = "이미지 주소")
    String image;

    @Schema(description = "태그")
    String tags;

    @Schema(description = "제목")
    String name;

    @Schema(description = "사고시각")
    LocalDateTime accidentAt;

    @Schema(description = "사고U아이디")
    String accidentUid;

    @Schema(description = "사고이유")
    String accidentReason;

    @Schema(description = "사고원인")
    String accidentCause;

    @Schema(description = "이유상세")
    String causeDetail;

    @Schema(description = "반응?")
    String response;
}
