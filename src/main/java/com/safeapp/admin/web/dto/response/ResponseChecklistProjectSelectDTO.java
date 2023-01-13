package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "체크리스트 단건 조회")
public class ResponseChecklistProjectSelectDTO {
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

    @Schema(description = "로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크")
    YN liked;

    @Schema(description = "상세내용")
    List<ResponseChecklistProjectDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseChecklistProjectSelectDTO(ChecklistProject checklistProject) {
        this.id = checklistProject.getId();
        this.name = checklistProject.getName();
        this.userName = checklistProject.getUser().getUserName();
        this.createdDate = checklistProject.getCreatedAt();
        this.views = checklistProject.getViews();
        this.likeCount = checklistProject.getLikes();
        this.tag = checklistProject.getTag();
        this.relatedAcidNo = checklistProject.getRelatedAcidNo();
        if(checklistProject.getChecker() != null) {
            this.checkerId = checklistProject.getChecker().getId();
            this.checkerName = checklistProject.getChecker().getUserName();
        }

        if(checklistProject.getReviewer() != null) {
            this.reviewerId = checklistProject.getReviewer().getId();
            this.reviewerName = checklistProject.getReviewer().getUserName();
        }

        if(checklistProject.getProject() != null) {
            this.projectId = checklistProject.getProject().getId();
        }

        if(checklistProject.getApprover() != null) {
            this.approverId = checklistProject.getApprover().getId();
            this.approverName = checklistProject.getApprover().getUserName();
        }

        this.recheckReason = checklistProject.getRecheckReason();
        this.checkAt = checklistProject.getCheckAt();
        this.reviewAt = checklistProject.getReview_at();
        this.approveAt = checklistProject.getApprove_at();
        this.status = checklistProject.getStatus();
        this.liked = checklistProject.getLiked();
        if (checklistProject.getChecklistProjectDetailList().isEmpty() == false) {
            for (ChecklistProjectDetail detail : checklistProject.getChecklistProjectDetailList()) {
                ResponseChecklistProjectDetailDTO dto = ResponseChecklistProjectDetailDTO
                        .builder()
                        .detail(detail)
                        .build();
                details.add(dto);
            }
        }
    }
}
