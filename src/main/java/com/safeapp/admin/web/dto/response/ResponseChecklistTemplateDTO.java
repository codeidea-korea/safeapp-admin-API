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
public class ResponseChecklistTemplateDTO {

    @Schema(description = "고유 아이디")
    Long id;
    @Schema(description = "제목")
    String title;
    @Schema(description = "프로젝트ID")
    Long projectId;

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
    public ResponseChecklistTemplateDTO(ChecklistTemplate template) {
        this.id = template.getId();
        this.title = template.getName();
        if(template.getProject() != null){
            this.projectId = template.getProject().getId();
        }
        this.userName = template.getUser().getUserName();
        this.createdDate = template.getCreatedAt();
        this.views = template.getViews();
        this.likeCount = template.getLikes();
        if(template.getDetails().isEmpty() == false) {
            for(ChecklistTemplateDetail detail : template.getDetails()){
                this.content.add(detail.getContents());
            }
        }
    }
}
