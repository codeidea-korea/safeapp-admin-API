package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체크리스트 템플릿 정보 확인 응답")
@Data
public class ResponseCheckListTemplateSelectDTO {

    @Schema(description = "PK")
    Long id;
    
    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "등록일시")
    LocalDateTime createdDate;

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "좋아요 수")
    Integer likeCount;

    @Schema(description = "관련 사고사례")
    String relatedAcidNo;

    @Schema(description = "태그")
    String tag;

    @Schema(description = "점검자 ID")
    Long checkerId;

    @Schema(description = "점검자 이름")
    String checkerName;

    @Schema(description = "검토자 ID")
    Long reviewerId;

    @Schema(description = "검토자 이름")
    String reviewerName;

    @Schema(description = "승인자 ID")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

    @Schema(description = "상세")
    List<ResponseCheckListTemplateDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseCheckListTemplateSelectDTO(CheckListTemplate template) {
        this.id = template.getId();
        this.name = template.getName();
        this.userName = template.getUser().getUserName();
        this.createdDate = template.getCreatedAt();
        this.views = template.getViews();
        this.likeCount = template.getLikes();
        this.tag = template.getTag();
        this.relatedAcidNo = template.getRelatedAcidNo();

        if(template.getProject() != null) {
            this.projectId = template.getProject().getId();
        }
        if(template.getChecker() != null) {
            this.checkerId = template.getChecker().getId();
            this.checkerName = template.getChecker().getUserName();
        }
        if(template.getReviewer() != null) {
            this.reviewerId = template.getReviewer().getId();
            this.reviewerName = template.getReviewer().getUserName();
        }
        if(template.getApprover() != null) {
            this.approverId = template.getApprover().getId();
            this.approverName = template.getApprover().getUserName();
        }

        if (template.getDetails().isEmpty() == false) {
            for(CheckListTemplateDetail detail : template.getDetails()) {
                ResponseCheckListTemplateDetailDTO dto =
                        ResponseCheckListTemplateDetailDTO
                        .builder()
                        .detail(detail)
                        .build();

                details.add(dto);
            }
        }
    }

}