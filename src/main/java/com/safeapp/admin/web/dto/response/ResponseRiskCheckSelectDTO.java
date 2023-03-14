package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "위험성 평가표 단독 조회 응답")
@Data
public class ResponseRiskCheckSelectDTO {

    @Schema(description = "위험성 평가표 PK")
    Long id;

    @Schema(description = "등록자(관리자) ID")
    String adminId;

    @Schema(description = "등록자(관리자) 이름")
    String adminName;

    @Schema(description = "등록자(사용자) ID")
    String userId;

    @Schema(description = "등록자(사용자) 이름")
    String userName;

    @Schema(description = "등록자 프로필 이미지")
    String image;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "등록일시")
    LocalDateTime createdDate;

    @Schema(description = "제목")
    String name;

    @Schema(description = "조회수")
    int views;

    @Schema(description = "좋아요 수")
    int likes;

    @Schema(description = "태그")
    String tag;

    @Schema(description = "관련 사고사례")
    String relatedAcidNo;

    @Schema(description = "점검자 PK")
    Long checkerId;

    @Schema(description = "이행 담당자 PK")
    Long dueUserId;

    @Schema(description = "이행 담당자 이름")
    String dueUserName;

    @Schema(description = "점검 담당자 PK")
    Long checkUserId;

    @Schema(description = "점검 담당자 이름")
    String checkUserName;

    @Schema(description = "점검자 이름")
    String checkerName;

    @Schema(description = "점검일시")
    LocalDateTime checkAt;

    @Schema(description = "검토자 1 PK")
    Long reviewer1Id;

    @Schema(description = "검토자 1 이름")
    String reviewer1Name;

    @Schema(description = "검토자 1 검토일시")
    LocalDateTime review1At;

    @Schema(description = "검토자 2 PK")
    Long reviewer2Id;

    @Schema(description = "검토자 2 이름")
    String reviewer2Name;

    @Schema(description = "검토자 2 검토일시")
    LocalDateTime review2At;

    @Schema(description = "검토자 3 PK")
    Long reviewer3Id;

    @Schema(description = "검토자 3 이름")
    String reviewer3Name;

    @Schema(description = "검토자 3 검토일시")
    LocalDateTime review3At;

    @Schema(description = "승인자 PK")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

    @Schema(description = "승인일시")
    LocalDateTime approveAt;

    @Schema(description = "작업개요 - 세부공종")
    String instructWork;

    @Schema(description = "공개여부")
    YN visibled;

    @Schema(description = "작업개요 - 작업공종")
    String instructDetail;

    @Schema(description = "작업 시작일시")
    LocalDateTime workStartAt;

    @Schema(description = "작업 종료일시")
    LocalDateTime workEndAt;

    @Schema(description = "기타 위험성 메모")
    String etcRiskMemo;

    @Schema(description = "상태")
    StatusType status;

    @Schema(description = "재검토 사유")
    String recheckReason;

    @Schema(description = "위험성 평가표 상세")
    List<ResponseRiskCheckDetailDTO> details = new ArrayList<>();

    @Schema(description = "검토자 목록")
    List<Reviewer> reviewers = new ArrayList<>();

    @Data
    public class Reviewer {

        @Schema(description = "검토자 PK")
        Long reviewerId;

        @Schema(description = "검토자 이름")
        String reviewerName;

        @Schema(description = "검토일시")
        LocalDateTime reviewAt;

        @Schema(description = "순서")
        Integer index;

        public Reviewer(Long id, String name, LocalDateTime time, Integer index) {

        }

    }

    @Builder
    public ResponseRiskCheckSelectDTO(RiskCheck riskCheck) {
        this.id = riskCheck.getId();
        this.name = riskCheck.getName();
        if(riskCheck.getAdmin() != null) {
            this.adminId = riskCheck.getAdmin().getAdminId();
            this.adminName = riskCheck.getAdmin().getAdminName();
        }
        if(riskCheck.getUser() != null) {
            this.userId = riskCheck.getUser().getUserId();
            this.userName = riskCheck.getUser().getUserName();
            this.image = riskCheck.getUser().getImage();
        }
        this.createdDate = riskCheck.getCreatedAt();
        this.views = riskCheck.getViews();
        this.likes = riskCheck.getLikes();
        this.tag = riskCheck.getTag();
        this.relatedAcidNo = riskCheck.getRelatedAcidNo();

        this.visibled = riskCheck.getVisibled();
        this.instructWork = riskCheck.getInstructWork();
        this.instructDetail = riskCheck.getInstructDetail();
        this.workStartAt = riskCheck.getWorkStartAt();
        this.workEndAt = riskCheck.getWorkEndAt();
        this.etcRiskMemo = riskCheck.getEtcRiskMemo();
        this.status = riskCheck.getStatus();
        this.recheckReason = riskCheck.getRecheckReason();
        this.checkAt = riskCheck.getCheckAt();
        this.approveAt = riskCheck.getApproveAt();

        if(riskCheck.getProject() != null) {
            this.projectId = riskCheck.getProject().getId();
        }
        if(riskCheck.getChecker() != null) {
            this.checkerId = riskCheck.getChecker().getId();
            this.checkerName = riskCheck.getChecker().getUserName();
            this.checkAt = riskCheck.getCheckAt();
        }
        if(riskCheck.getApprover() != null) {
            this.approverId = riskCheck.getApprover().getId();
            this.approverName = riskCheck.getApprover().getUserName();
            this.approveAt = riskCheck.getApproveAt();
        }
        if(riskCheck.getReviewer1() != null) {
            this.reviewer1Id = riskCheck.getReviewer1().getId();
            this.reviewer1Name = riskCheck.getReviewer1().getUserName();
            this.review1At = riskCheck.getReview1_at();
        }
        if(riskCheck.getReviewer2() != null) {
            this.reviewer2Id = riskCheck.getReviewer2().getId();
            this.reviewer2Name = riskCheck.getReviewer2().getUserName();
            this.review2At = riskCheck.getReview2_at();
        }
        if(riskCheck.getReviewer3() != null) {
            this.reviewer3Id = riskCheck.getReviewer3().getId();
            this.reviewer3Name = riskCheck.getReviewer3().getUserName();
            this.review3At = riskCheck.getReview3_at();
        }
        if(riskCheck.getDueUser() != null) {
            this.dueUserId = riskCheck.getDueUser().getId();
            this.dueUserName = riskCheck.getDueUser().getUserName();
        }
        if(riskCheck.getCheckUser() != null) {
            this.checkUserId = riskCheck.getCheckUser().getId();
            this.checkUserName = riskCheck.getCheckUser().getUserName();
        }

        if(riskCheck.getRiskCheckDetailList().isEmpty() == false) {
            for(RiskCheckDetail detail : riskCheck.getRiskCheckDetailList()) {
                ResponseRiskCheckDetailDTO dto =
                    ResponseRiskCheckDetailDTO
                    .builder()
                    .detail(detail)
                    .build();

                details.add(dto);
            }
        }
    }

}