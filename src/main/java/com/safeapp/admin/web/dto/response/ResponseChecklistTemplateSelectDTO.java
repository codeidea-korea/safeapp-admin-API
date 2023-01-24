package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.ChecklistTemplate;
import com.safeapp.admin.web.model.entity.ChecklistTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "체크리스트 템플릿 단건 조회")
public class ResponseChecklistTemplateSelectDTO {
    @Schema(description = "고유 아이디")
    Long id;
    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "프로젝트ID")
    Long projectId;

    @Schema(description = "등록일")
    LocalDateTime createdDate;

    @Schema(description = "열람횟수")
    Integer views;

    @Schema(description = "좋아요 카운트")
    Integer likeCount;

    @Schema(description = "관련사고사례")
    String relatedAcidNo;

    @Schema(description = "태그")
    String tag;

    @Schema(description = "체크자ID")
    Long checkerId;

    @Schema(description = "체크자 이름")
    String checkerName;

    @Schema(description = "리뷰자ID")
    Long reviewerId;

    @Schema(description = "리뷰자 이름")
    String reviewerName;

    @Schema(description = "승인자 ID")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

    @Schema(description = "상세내용")
    List<ResponseCheckListTemplateDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseChecklistTemplateSelectDTO(ChecklistTemplate template) {
        this.id = template.getId();
        this.name = template.getName();
        this.userName = template.getUser().getUserName();
        this.createdDate = template.getCreatedAt();
        this.views = template.getViews();
        this.likeCount = template.getLikes();
        this.tag = template.getTag();
        this.relatedAcidNo = template.getRelatedAcidNo();
        if(template.getReviewer() != null){
            this.reviewerId = template.getReviewer().getId();
            this.reviewerName = template.getReviewer().getUserName();
        }
        if(template.getChecker()!=null){
            this.checkerId = template.getChecker().getId();
            this.checkerName = template.getChecker().getUserName();
        }
        if(template.getApprover() != null){
            this.approverId = template.getApprover().getId();
            this.approverName = template.getApprover().getUserName();
        }

        if(template.getProject() != null) {
            this.projectId = template.getProject().getId();
        }
        if (template.getDetails().isEmpty() == false) {
            for (ChecklistTemplateDetail detail : template.getDetails()) {
                ResponseCheckListTemplateDetailDTO dto = ResponseCheckListTemplateDetailDTO
                        .builder()
                        .detail(detail)
                        .build();
                details.add(dto);
            }
        }
    }
}
