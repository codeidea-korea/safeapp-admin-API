package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체크리스트 단독 조회 응답")
@Data
public class ResponseCheckListProjectSelectDTO {

    @Schema(description = "체크리스트 PK")
    Long id;

    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자(관리자) PK")
    Long adminId;

    @Schema(description = "등록자(관리자) 이름")
    String adminName;

    @Schema(description = "등록자(사용자) PK")
    Long userId;

    @Schema(description = "등록자(사용자) 이름")
    String userName;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "등록자 프로필 이미지")
    String image;

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

    @Schema(description = "점검자 PK")
    Long checkerId;

    @Schema(description = "점검자 이름")
    String checkerName;

    @Schema(description = "검토자 PK")
    Long reviewerId;

    @Schema(description = "검토자 이름")
    String reviewerName;

    @Schema(description = "승인자 PK")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

    @Schema(description = "재검토 사유")
    String recheckReason;

    @Schema(description = "점검시간")
    LocalDateTime checkAt;

    @Schema(description = "검토시간")
    LocalDateTime reviewAt;

    @Schema(description = "승인시간")
    LocalDateTime approveAt;

    @Schema(description = "상태")
    StatusType status;

    @Schema(description = "공개상태")
    YN visibled;

    @Schema(description = "체크리스트 상세")
    List<ResponseCheckListProjectDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseCheckListProjectSelectDTO(CheckListProject checkListProject) {
        this.id = checkListProject.getId();
        this.name = checkListProject.getName();
        if(checkListProject.getAdmin() != null) {
            this.adminId = checkListProject.getAdmin().getId();
            this.adminName = checkListProject.getAdmin().getAdminName();
        }
        if(checkListProject.getUser() != null) {
            this.userId = checkListProject.getUser().getId();
            this.userName = checkListProject.getUser().getUserName();
        }
        this.createdDate = checkListProject.getCreatedAt();
        this.views = checkListProject.getViews();
        this.likeCount = checkListProject.getLikes();
        this.relatedAcidNo = checkListProject.getRelatedAcidNo();
        this.tag = checkListProject.getTag();
        this.recheckReason = checkListProject.getRecheckReason();
        this.checkAt = checkListProject.getCheckAt();
        this.reviewAt = checkListProject.getReviewAt();
        this.approveAt = checkListProject.getApproveAt();
        this.status = checkListProject.getStatus();
        this.visibled = checkListProject.getVisibled();

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

        if(checkListProject.getCheckListProjectDetailList().isEmpty() == false) {
            for(CheckListProjectDetail detail : checkListProject.getCheckListProjectDetailList()) {
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