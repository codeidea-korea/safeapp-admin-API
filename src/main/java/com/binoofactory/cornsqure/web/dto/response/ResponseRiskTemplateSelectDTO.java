package com.binoofactory.cornsqure.web.dto.response;

import com.binoofactory.cornsqure.web.data.StatusType;
import com.binoofactory.cornsqure.web.model.entity.RiskCheck;
import com.binoofactory.cornsqure.web.model.entity.RiskCheckDetail;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplate;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "위험요소템플릿 단건조회")
public class ResponseRiskTemplateSelectDTO {

    @Schema(description = "고유 아이디")
    Long id;

    @Schema(description = "유저 아이디")
    Long userId;

    @Schema(description = "유저 이름")
    String userName;

    @Schema(description = "프로젝트ID")
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

    @Schema(description = "리뷰어 ID")
    Long reviewerId;

    @Schema(description = "리뷰어 이름")
    String reviewerName;

    @Schema(description = "승인자 ID")
    Long approverId;

    @Schema(description = "승인자 이름")
    String approverName;

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

    @Schema(description = "상세내용")
    List<ResponseRiskTemplateDetailDTO> details = new ArrayList<>();

    @Builder
    public ResponseRiskTemplateSelectDTO(RiskTemplate template) {
        this.id = template.getId();
        this.name = template.getName();
        this.userName = template.getUser().getUserName();
        this.createdDate = template.getCreatedAt();
        this.userId = template.getUser().getId();
        this.views = template.getViews();
        this.likes = template.getLikes();

        if(template.getProject() != null) {
            this.projectId = template.getProject().getId();
        }
        this.tag = template.getTag();
        this.relatedAcidNo = template.getRelatedAcidNo();
        this.checkerId = template.getChecker().getId();
        this.checkerName = template.getChecker().getUserName();
        this.reviewerId = template.getReviewer().getId();
        this.reviewerName = template.getReviewer().getUserName();
        this.approverId = template.getApprover().getId();
        this.approverName = template.getApprover().getUserName();
        this.instructWork = template.getInstructWork();
        this.instructDetail = template.getInstructDetail();
        this.workStartAt = template.getWorkStartAt();
        this.workEndAt = template.getWorkEndAt();
        this.etcRiskMemo = template.getEtcRiskMemo();
        if(template.getDetails().isEmpty() == false) {
            for (RiskTemplateDetail detail : template.getDetails()){
                ResponseRiskTemplateDetailDTO dto = ResponseRiskTemplateDetailDTO
                        .builder()
                        .detail(detail)
                        .build();
                details.add(dto);
            }
        }
    }

}
