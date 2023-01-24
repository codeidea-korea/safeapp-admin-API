package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체크리스트 정보 확인")
@Data
public class ResponseCheckListProjectSelectDTO {

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

    @Schema(description = "관련사고사례")
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

    @Schema(description = "재검토사유")
    String recheckReason;

    @Schema(description = "체크시간")
    LocalDateTime checkAt;

    @Schema(description = "검토시간")
    LocalDateTime reviewAt;

    @Schema(description = "승인시간")
    LocalDateTime approveAt;

    @Schema(description = "상태")
    StatusType status;

    @Schema(description = "상세")
    List<ResponseCheckListProjectDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseCheckListProjectSelectDTO(CheckListProject checkListProject) {
        this.id = checkListProject.getId();
        this.name = checkListProject.getName();
        this.userName = checkListProject.getUser().getUserName();
        this.createdDate = checkListProject.getCreatedAt();
        this.views = checkListProject.getViews();
        this.likeCount = checkListProject.getLikes();
        this.relatedAcidNo = checkListProject.getRelatedAcidNo();
        this.tag = checkListProject.getTag();
        this.recheckReason = checkListProject.getRecheckReason();
        this.checkAt = checkListProject.getCheckAt();
        this.reviewAt = checkListProject.getReview_at();
        this.approveAt = checkListProject.getApprove_at();
        this.status = checkListProject.getStatus();

        if(checkListProject.getProject() != null) {
            this.projectId = checkListProject.getProject().getId();
        }
        if(checkListProject.getChecker() != null) {
            this.checkerId = checkListProject.getChecker().getId();
            this.checkerName = checkListProject.getChecker().getUserName();
        }
        if(checkListProject.getReviewer() != null) {
            this.reviewerId = checkListProject.getReviewer().getId();
            this.reviewerName = checkListProject.getReviewer().getUserName();
        }
        if(checkListProject.getApprover() != null) {
            this.approverId = checkListProject.getApprover().getId();
            this.approverName = checkListProject.getApprover().getUserName();
        }

        if(checkListProject.getChecklistProjectDetailList().isEmpty() == false) {
            for(ChecklistProjectDetail detail : checkListProject.getChecklistProjectDetailList()) {
                ResponseCheckListProjectDetailDTO chkPrjDetDto =
                        ResponseCheckListProjectDetailDTO
                        .builder()
                        .detail(detail)
                        .build();

                details.add(chkPrjDetDto);
            }
        }
    }

}