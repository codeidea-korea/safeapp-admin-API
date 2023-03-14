package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.RiskCheck;
import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "위험성 평가표 응답")
@Data
@NoArgsConstructor
public class ResponseRiskCheckDTO {

    @Schema(description = "위험성 평가표 PK")
    Long id;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자(관리자) ID")
    String adminId;

    @Schema(description = "등록자(관리자) 이름")
    String adminName;

    @Schema(description = "등록자(사용자) ID")
    String userId;

    @Schema(description = "등록자(사용자) 이름")
    String userName;

    @Schema(description = "등록일시")
    LocalDateTime createdDate;

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "좋아요 수")
    Integer likeCount;

    @Schema(description = "본문")
    List<String> contents = new ArrayList<>();

    @Schema(description = "검토자 1 검토일시")
    LocalDateTime reviewer_at1;

    @Schema(description = "검토자 2 검토일시")
    LocalDateTime reviewer_at2;

    @Schema(description = "검토자 3 검토일시")
    LocalDateTime reviewer_at3;

    @Schema(description = "공개여부")
    YN visibled;

    @Builder
    public ResponseRiskCheckDTO(RiskCheck riskCheck, List<String> contents) {
        this.id = riskCheck.getId();
        this.name = riskCheck.getName();
        if(riskCheck.getAdmin() != null) {
            this.adminId = riskCheck.getAdmin().getAdminId();
            this.adminName = riskCheck.getAdmin().getAdminName();
        }
        if(riskCheck.getUser() != null) {
            this.userId = riskCheck.getUser().getUserId();
            this.userName = riskCheck.getUser().getUserName();
        }
        this.createdDate = riskCheck.getCreatedAt();
        this.views = riskCheck.getViews();
        this.likeCount = riskCheck.getLikes();

        this.reviewer_at1 = riskCheck.getReview1_at();
        this.reviewer_at2 = riskCheck.getReview2_at();
        this.reviewer_at3 = riskCheck.getReview3_at();

        this.visibled = riskCheck.getVisibled();

        if(riskCheck.getProject() != null) {
            this.projectId = riskCheck.getProject().getId();
        }
        if(contents != null) {
            this.contents = contents;
        }
    }

}