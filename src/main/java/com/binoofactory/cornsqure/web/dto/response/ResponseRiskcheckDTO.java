package com.binoofactory.cornsqure.web.dto.response;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.entity.RiskCheck;
import com.binoofactory.cornsqure.web.model.entity.RiskCheckDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "위험성체크 메인")
public class ResponseRiskcheckDTO {
    @Schema(description = "고유 아이디")
    Long id;

    @Schema(description = "프로젝트 아이디")
    Long projectId;
    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "등록일")
    LocalDateTime createdDate;

    @Schema(description = "열람횟수")
    Integer views;

    @Schema(description = "좋아요 카운트")
    Integer likeCount;

    @Schema(description = "로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크")
    YN liked;

    @Schema(description = "본문")
    List<String> content = new ArrayList<>();

    @Builder
    public ResponseRiskcheckDTO(RiskCheck riskCheck) {
        this.id = riskCheck.getId();
        this.name = riskCheck.getName();
        this.userName = riskCheck.getUser().getUserName();
        if(riskCheck.getProject() != null){
            this.projectId = riskCheck.getProject().getId();
        }
        this.createdDate = riskCheck.getCreatedAt();
        this.views = riskCheck.getViews();
        this.likeCount = riskCheck.getLikes();
        this.liked = riskCheck.getLiked();
        if(riskCheck.getRiskCheckDetailList().isEmpty() == false) {
            for (RiskCheckDetail detail : riskCheck.getRiskCheckDetailList()) {
                this.content.add(detail.getContents());
            }
        }
    }

}
