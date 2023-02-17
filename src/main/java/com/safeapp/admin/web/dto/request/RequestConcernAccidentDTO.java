package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "아차사고 등록 요청")
@Data
public class RequestConcernAccidentDTO {

    @Schema(description = "제목")
    String title;

    @Schema(description = "등록자(관리자) PK")
    Long adminId;

    @Schema(description = "태그")
    String tags;

    @Schema(description = "작업내용")
    String name;

    @Schema(description = "작업자")
    String accidentUserName;

    @Schema(description = "발생유형")
    String accidentType;

    @Schema(description = "발생장소")
    String accidentPlace;

    @Schema(description = "발생개요 및 예상피해")
    String causeDetail;

    @Schema(description = "발생원인")
    String accidentCause;

    @Schema(description = "관리대책")
    String response;

    @Schema(description = "이미지 첨부")
    String image;

}