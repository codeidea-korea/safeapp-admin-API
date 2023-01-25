package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.CheckListTemplate;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체크리스트 템플릿 응답")
@Data
public class ResponseCheckListTemplateDTO {

    @Schema(description = "체크리스트 템플릿 PK")
    Long id;

    @Schema(description = "제목")
    String title;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "등록일시")
    LocalDateTime createdDate;

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "좋아요 수")
    Integer likeCount;

    @Schema(description = "본문")
    List<String> content = new ArrayList<>();

    @Builder
    public ResponseCheckListTemplateDTO(CheckListTemplate template) {
        this.id = template.getId();
        this.title = template.getName();
        this.userName = template.getUser().getUserName();
        this.createdDate = template.getCreatedAt();
        this.views = template.getViews();
        this.likeCount = template.getLikes();

        if(template.getProject() != null) {
            this.projectId = template.getProject().getId();
        }

        if(template.getDetails().isEmpty() == false) {
            for(CheckListTemplateDetail detail : template.getDetails()) {
                this.content.add(detail.getContents());
            }
        }
    }

}