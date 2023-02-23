package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.ConcernAccidentExp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "아차사고 응답")
@Data
@NoArgsConstructor
public class ResponseConcernAccidentDTO {

    @Schema(description = "아차사고 PK")
    Long id;

    @Schema(description = "등록자(관리자) 이름")
    @NotBlank(message = NOT_NULL)
    String adminName;

    @Schema(description = "등록자(사용자) 이름")
    @NotBlank(message = NOT_NULL)
    String userName;

    @Schema(description = "등록일시")
    LocalDateTime createdAt;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

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
    String accidentReason;

    @Schema(description = "관리대책")
    String response;

    @Schema(description = "이미지 첨부")
    String image;

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "사고경위")
    String accidentCause;

    @Schema(description = "사고발생일시")
    LocalDateTime accidentAt;

    @Builder
    public ResponseConcernAccidentDTO(ConcernAccidentExp conExp) {
        this.id = conExp.getId();
        this.adminName = conExp.getAdmin().getAdminName();
        this.userName = conExp.getUser().getUserName();
        this.createdAt = conExp.getCreatedAt();
        this.title = conExp.getTitle();
        this.tags = conExp.getTags();
        this.name = conExp.getName();
        this.accidentUserName = conExp.getAccidentUserName();
        this.accidentType = conExp.getAccidentType();
        this.accidentPlace = conExp.getAccidentPlace();
        this.causeDetail = conExp.getCauseDetail();
        this.accidentReason = conExp.getAccidentReason();
        this.response = conExp.getResponse();
        this.image = conExp.getImage();
        this.views = conExp.getViews();
        this.accidentCause = conExp.getAccidentCause();
        this.accidentAt = conExp.getAccidentAt();
    }

}