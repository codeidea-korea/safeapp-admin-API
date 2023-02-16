package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Admins;
import com.safeapp.admin.web.model.entity.CheckListProject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "사고사례 응답")
@Data
@NoArgsConstructor
@Slf4j
public class ResponseAccidentCaseDTO {

    @Schema(description = "사고사례 PK")
    @NotBlank(message = NOT_NULL)
    Long id;

    @Schema(description = "등록일시")
    LocalDateTime createdAt;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

    @Schema(description = "등록자(관리자) 이름")
    @NotBlank(message = NOT_NULL)
    String adminName;

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
    @NotBlank(message = NOT_NULL)
    Integer views;

    @Schema(description = "사고일련번호")
    String accidentUid;

    @Builder
    public ResponseAccidentCaseDTO(AccidentExp accExp) {
        this.id = accExp.getId();
        this.createdAt = accExp.getCreatedAt();
        this.title = accExp.getTitle();
        this.adminName = accExp.getAdmin().getAdminName();
        this.tags = accExp.getTags();
        this.name = accExp.getName();
        this.accidentAt = accExp.getAccidentAt();
        this.accidentReason = accExp.getAccidentReason();
        this.accidentCause = accExp.getAccidentCause();
        this.causeDetail = accExp.getCauseDetail();
        this.response = accExp.getResponse();
        this.image = accExp.getImage();
        this.views = accExp.getViews();
        this.accidentUid = accExp.getAccidentUid();
    }

}