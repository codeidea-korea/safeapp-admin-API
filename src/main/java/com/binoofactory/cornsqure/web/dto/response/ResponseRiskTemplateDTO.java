package com.binoofactory.cornsqure.web.dto.response;

import com.binoofactory.cornsqure.web.model.entity.RiskCheck;
import com.binoofactory.cornsqure.web.model.entity.RiskCheckDetail;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplate;
import com.binoofactory.cornsqure.web.model.entity.RiskTemplateDetail;
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
@Schema(description = "위험성체크 템플릿 메인")
public class ResponseRiskTemplateDTO {
    @Schema(description = "고유 아이디")
    Long id;
    @Schema(description = "프로젝트 아이디")
    Long projectId;
    @Schema(description = "제목")
    String title;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "등록일")
    LocalDateTime createdDate;

    @Schema(description = "열람횟수")
    Integer views;

    @Schema(description = "좋아요 카운트")
    Integer likeCount;

    @Schema(description = "본문")
    List<String> content = new ArrayList<>();

    @Builder
    public ResponseRiskTemplateDTO(RiskTemplate template) {
        this.id = template.getId();
        this.title = template.getName();
        this.userName = template.getUser().getUserName();
        if(template.getProject().getId() != null){
            this.projectId = template.getProject().getId();
        }
        this.createdDate = template.getCreatedAt();
        this.views = template.getViews();
        this.likeCount = template.getLikes();
        if(template.getDetails().isEmpty() == false) {
            for (RiskTemplateDetail detail : template.getDetails()) {
                this.content.add(detail.getContents());
            }
        }
    }

}
