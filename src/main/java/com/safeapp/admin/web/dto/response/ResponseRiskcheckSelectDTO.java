package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "위험요소 단건조회")
public class ResponseRiskcheckSelectDTO {

    @Schema(description = "고유 아이디")
    Long id;

    @Schema(description = "유저 아이디")
    Long userId;

    @Schema(description = "유저 이름")
    String userName;

    @Schema(description = "프로젝트 ID")
    Long projectId;

    @Schema(description = "등록일")
    LocalDateTime createdDate;

    @Schema(description = "제목")
    String name;

    @Schema(description = "조회수")
    int views;

    @Schema(description = "좋아요수")
    int likes;

    @Schema(description = "태그")
    String tag;

    @Schema(description = "관련사고사례")
    String relatedAcidNo;

    @Schema(description = "체커 ID")
    Long checkerId;

    @Schema(description = "체커 이름")
    String checkerName;

    @Schema(description = "체크 시간")
    LocalDateTime checkAt;

    @Schema(description = "리뷰어 ID")
    Long reviewerId;

    @Schema(description = "리뷰어 이름")
    String reviewerName;

    @Schema(description = "리뷰 시간")
    LocalDateTime reviewAt;

    @Schema(description = "승인자 ID")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

    @Schema(description = "승인 시간")
    LocalDateTime approveAt;

    @Schema(description = "작업개요 - 세부공종")
    String instructWork;

    @Schema(description = "작업개요 - 작업공종")
    String instructDetail;

    @Schema(description = "작업시작시간")
    LocalDateTime workStartAt;

    @Schema(description = "작업종료시간")
    LocalDateTime workEndAt;

    @Schema(description = "기타위험메모")
    String etcRiskMemo;

    @Schema(description = "상태")
    StatusType status;

    @Schema(description = "재검토사유")
    String recheckReason;

    @Schema(description = "로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크")
    YN liked;

    @Schema(description = "상세내용")
    List<ResponseRiskCheckDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseRiskcheckSelectDTO(RiskCheck riskCheck) {
        this.id = riskCheck.getId();
        this.name = riskCheck.getName();
        this.userName = riskCheck.getUser().getUserName();
        this.createdDate = riskCheck.getCreatedAt();
        this.userId = riskCheck.getUser().getId();
        this.views = riskCheck.getViews();
        this.likes = riskCheck.getLikes();
        this.tag = riskCheck.getTag();
        this.relatedAcidNo = riskCheck.getRelatedAcidNo();
        if(riskCheck.getProject() != null) {
            this.projectId = riskCheck.getProject().getId();
        }
        if(riskCheck.getChecker() != null) {
            this.checkerId = riskCheck.getChecker().getId();
            this.checkerName = riskCheck.getChecker().getUserName();
        }
        if(riskCheck.getReviewer() != null){
            this.reviewerId = riskCheck.getReviewer().getId();
            this.reviewerName = riskCheck.getReviewer().getUserName();
        }

        if(riskCheck.getApprover() != null) {
            this.approverId = riskCheck.getApprover().getId();
            this.approverName = riskCheck.getApprover().getUserName();
        }
        this.instructWork = riskCheck.getInstructWork();
        this.instructDetail = riskCheck.getInstructDetail();
        this.workStartAt = riskCheck.getWorkStartAt();
        this.workEndAt = riskCheck.getWorkEndAt();
        this.etcRiskMemo = riskCheck.getEtcRiskMemo();
        this.status = riskCheck.getStatus();
        this.recheckReason = riskCheck.getRecheckReason();
        this.checkAt = riskCheck.getCheckAt();
        this.reviewAt = riskCheck.getReviewAt();
        this.approveAt = riskCheck.getApproveAt();
        this.liked = riskCheck.getLiked();
        if(riskCheck.getRiskCheckDetailList().isEmpty() == false) {
            for (RiskCheckDetail detail : riskCheck.getRiskCheckDetailList()){
                ResponseRiskCheckDetailDTO dto = ResponseRiskCheckDetailDTO
                        .builder()
                        .detail(detail)
                        .build();
                details.add(dto);
            }
        }
    }

}
